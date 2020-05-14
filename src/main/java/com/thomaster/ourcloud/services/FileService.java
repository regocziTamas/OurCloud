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
import com.thomaster.ourcloud.services.request.save.file.PreFlightSaveFileRequest;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequest;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FileService {

    private static final Pattern PATH_FRIENDLY_FILENAME = Pattern.compile("[A-Za-z0-9|_]");

    FileRepository fileRepository;
    OCUserService userService;
    FileSystemService fileSystemService;
    PreFlightSaveFileRequestTokenService tokenService;

    public FileService(FileRepository fileRepository,
                       OCUserService userService,
                       FileSystemService fileSystemService,
                       PreFlightSaveFileRequestTokenService tokenService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
        this.fileSystemService = fileSystemService;
        this.tokenService = tokenService;
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

        return fileRepository.findOneByPathWithContainedFiles(pathToFileToRead);
    }

    @PreQueryRequestValidation(PreQueryRequestValidationType.DELETE)
    public void deleteFSElementRecursively(DeleteRequest deleteRequest) {
        Set<String> filenamesToDeleteOnDisk = fileRepository.deleteRecursivelyByPath(deleteRequest.getFileToDelete().getRelativePath());

        fileRepository.updateFileSizeAllAncestorFolders(deleteRequest.getParentFolder().getRelativePath(), deleteRequest.getFileToDelete().getFileSize() * -1);
    }

    @PreQueryRequestValidation(PreQueryRequestValidationType.UPLOAD_FOLDER)
    public void saveFolder(SaveFolderRequest request) {
        UploadedFolder newFolder = new UploadedFolder();
        newFolder.setOwner(request.getInitiatingUser().get());
        newFolder.setParentFolderPath(request.getParentFolder().getRelativePath());
        newFolder.setOriginalName(request.getOriginalName());
        newFolder.setRelativePath(request.getParentFolder().getRelativePath() + "." + request.getOriginalName());

        fileRepository.save(newFolder);
    }


    @PreQueryRequestValidation(PreQueryRequestValidationType.UPLOAD_FILE)
    public void saveFile(SaveFileRequest request) {

        PreFlightSaveFileRequest preFlightSaveFileRequest = request.getPreFlightSaveFileRequest();

        UploadedFile newFile = new UploadedFile();
        newFile.setOwner(request.getInitiatingUser().get());
        newFile.setParentFolderPath(preFlightSaveFileRequest.getParentFolder().getRelativePath());
        newFile.setOriginalName(preFlightSaveFileRequest.getOriginalName());
        newFile.setFileSize(preFlightSaveFileRequest.getSize());
        newFile.setRelativePath(preFlightSaveFileRequest.getParentFolder().getRelativePath() + "." + makeNamePathFriendly(preFlightSaveFileRequest.getOriginalName()));
        newFile.setMimeType(request.getMimeType());

        String filenameOnDisk = UUID.randomUUID().toString();
        newFile.setFilenameOnDisk(filenameOnDisk);

        fileSystemService.writeFile(request.getFileToUpload(), filenameOnDisk);

        fileRepository.save(newFile);
        fileRepository.updateFileSizeAllAncestorFolders(preFlightSaveFileRequest.getParentFolder().getRelativePath(), preFlightSaveFileRequest.getSize());

        tokenService.removeToken(request.getUploadToken());
    }

    @PreQueryRequestValidation(PreQueryRequestValidationType.PREFLIGHT_UPLOAD_FLIGHT)
    public String registerPreFlightSaveFileRequest(PreFlightSaveFileRequest request) {
        return tokenService.getTokenForPreFlightRequest(request);
    }

    private String makeNamePathFriendly(String originalName){
        return Arrays.stream(originalName.split(""))
                .map(character -> {
                    if (PATH_FRIENDLY_FILENAME.matcher(character).matches())
                        return character;
                    return "_";
                })
                .collect(Collectors.joining());
    }

    @PostQueryRequestValidation(PostQueryRequestValidationType.DOWNLOAD)
    public FileSystemElement queryFileToDownload(String pathToFileToRead) {
        return fileRepository.findOneByPathWithoutContainedFiles(pathToFileToRead);
    }
}
