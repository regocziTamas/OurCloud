package com.thomaster.ourcloud.services;

import com.google.common.base.Strings;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.repositories.file.FileRepository;
import com.thomaster.ourcloud.services.request.marker.PostQueryRequestValidation;
import com.thomaster.ourcloud.services.request.marker.PostQueryRequestValidationType;
import com.thomaster.ourcloud.services.request.marker.PreQueryRequestValidationType;
import com.thomaster.ourcloud.services.request.marker.PreQueryRequestValidation;
import com.thomaster.ourcloud.services.request.delete.DeleteRequest;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequest;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequest;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    FilePathService filePathService;
    FileRepository fileRepository;
    OCUserService userService;
    FileSystemService fileSystemService;

    public FileService(FilePathService filePathService,
                       FileRepository fileRepository,
                       OCUserService userService,
                       FileSystemService fileSystemService) {
        this.filePathService = filePathService;
        this.fileRepository = fileRepository;
        this.userService = userService;
        this.fileSystemService = fileSystemService;
    }

    @PostQueryRequestValidation(PostQueryRequestValidationType.READ)
    public FileSystemElement findFSElementWithContainedFilesByPath(String pathToFileToRead) {
        return findFSE(pathToFileToRead);
    }

    public FileSystemElement findFSElementWithContainedFilesByPath_noChecks(String pathToFileToRead) {
        return findFSE(pathToFileToRead);
    }

    private FileSystemElement findFSE(String pathToFileToRead) {
        if (Strings.isNullOrEmpty(pathToFileToRead))
            return null;

        return fileRepository.findOneByPath(pathToFileToRead);
    }

    @PreQueryRequestValidation(PreQueryRequestValidationType.DELETE)
    public void deleteFSElementRecursively(DeleteRequest deleteRequest) {
        fileRepository.deleteRecursivelyByPath(deleteRequest.getFileToDelete().getRelativePath());
    }

    @PreQueryRequestValidation(PreQueryRequestValidationType.UPLOAD_FOLDER)
    public void saveFolder(SaveFolderRequest request) {
        UploadedFolder newFolder = new UploadedFolder();
        newFolder.setOwner(request.getInitiatingUser().get());
        newFolder.setParentFolderPath(request.getParentFolder().getRelativePath());
        newFolder.setOriginalName(request.getOriginalName());
        newFolder.setRelativePath(request.getParentFolder().getParentFolderPath() + "." + request.getOriginalName());

        fileRepository.save(newFolder);
    }

    @PreQueryRequestValidation(PreQueryRequestValidationType.UPLOAD_FILE)
    public void saveFile(SaveFileRequest request) {
        UploadedFile newFile = new UploadedFile();
        newFile.setOwner(request.getInitiatingUser().get());
        newFile.setParentFolderPath(request.getParentFolder().getRelativePath());
        newFile.setOriginalName(request.getOriginalName());
        newFile.setFileSize(request.getSize());
        newFile.setRelativePath(request.getParentFolder().getParentFolderPath() + "." + request.getOriginalName());

        fileSystemService.writeFile(request.getFile());

        fileRepository.save(newFile);
        fileRepository.updateFileSizeAllAncestorFolders(request.getParentFolder().getParentFolderPath(), request.getSize());
    }
}
