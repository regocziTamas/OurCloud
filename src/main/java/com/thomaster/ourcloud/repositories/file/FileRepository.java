package com.thomaster.ourcloud.repositories.file;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FileRepository {

    private static final String QUERY_POSTFIX_SEARCH_FIRST_LEVEL_ONLY = ".*{0,1}";
    private static final String QUERY_POSTFIX_SEARCH_ALL_LEVELS_RECURSIVELY = ".*";

    private FileRepository_Internal fileRepository;

    public FileRepository(FileRepository_Internal fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileSystemElement findOneByPathWithContainedFiles(String pathToSearch) {

        String relativePath = pathToSearch + QUERY_POSTFIX_SEARCH_FIRST_LEVEL_ONLY;

        Set<PersistentFileSystemElement> persistedResult = fileRepository.findOneByPath(relativePath);

        if (persistedResult.isEmpty())
            return null;

        return new PersistentFSEHierarchyConverter(persistedResult, pathToSearch).convertAndBuildHierarchy();
    };

    public FileSystemElement findOneByPathWithoutContainedFiles(String pathToSearch) {
        Set<PersistentFileSystemElement> persistedResult = fileRepository.findOneByPath(pathToSearch);

        if (persistedResult.isEmpty())
            return null;
        else if(persistedResult.size() > 1)
            throw new IllegalArgumentException("Not possible");
        else {
            return new PersistentFSEHierarchyConverter(persistedResult, pathToSearch).convertAndBuildHierarchy();
        }

    }

    public void deleteRecursivelyByPath(String pathToDelete) {
        String deleteQueryParam = pathToDelete + QUERY_POSTFIX_SEARCH_ALL_LEVELS_RECURSIVELY;

        fileRepository.deleteRecursivelyByPath(deleteQueryParam);
    };

    public void updateFileSizeAllAncestorFolders(String pathToUpdate, Long fileSizeDelta) {
        fileRepository.updateFileSizeAllAncestorFolders(pathToUpdate, fileSizeDelta);
    }

    @SuppressWarnings("unchecked")
    public <S extends FileSystemElement> S save(S fileSystemElement) {
        PersistentFileSystemElement persistentModel = PersistentToDomainConverter.convertToPersistent(fileSystemElement);
        PersistentFileSystemElement savedPersistedModel = fileRepository.save(persistentModel);

        return (S) PersistentToDomainConverter.convertToDomain(savedPersistedModel);
    }


}
