package com.thomaster.ourcloud.repositories.file;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
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

    public FileSystemElement findOneByPath(String pathToSearch) {

        String relativePath = pathToSearch + QUERY_POSTFIX_SEARCH_FIRST_LEVEL_ONLY;

        Set<PersistentFileSystemElement> persistedResult = fileRepository.findOneByPath(relativePath);

        if (persistedResult.isEmpty())
            return null;

        return new PersistentFSEHierarchyConverter(persistedResult, pathToSearch).convertAndBuildHierarchy();
    };

    public void deleteRecursivelyByPath(String pathToDelete) {
        String deleteQueryParam = pathToDelete + QUERY_POSTFIX_SEARCH_ALL_LEVELS_RECURSIVELY;

        fileRepository.deleteRecursivelyByPath(deleteQueryParam);
    };

    public void updateFileSizeAllAncestorFolders(String pathToUpdate, Long fileSizeDelta) {
        String updateQueryParam = pathToUpdate + QUERY_POSTFIX_SEARCH_ALL_LEVELS_RECURSIVELY;

        fileRepository.updateFileSizeAllAncestorFolders(updateQueryParam, fileSizeDelta);
    }

    public <S extends FileSystemElement> S save(S fileSystemElement) {
        return (S) PersistentToDomainConverter.convertToDomain(fileRepository.save(PersistentToDomainConverter.convertToPersistent(fileSystemElement)));
    }


}
