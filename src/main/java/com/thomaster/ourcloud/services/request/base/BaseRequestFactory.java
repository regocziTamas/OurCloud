package com.thomaster.ourcloud.services.request.base;


import com.google.common.base.Strings;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import org.springframework.stereotype.Component;

@Component
public class BaseRequestFactory<T extends BaseRequest> {

    protected OCUserService userService;
    protected FileService fileService;

    public BaseRequestFactory(OCUserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    public UploadedFolder validatePathThenQueryAndCastToFolder(String path) {

        FileSystemElement folder = fileService.findFSElementWithContainedFilesByPath_noChecks(path);

        if(folder == null)
            throw new IllegalArgumentException("No Folder exists with the following path: " + path);

        if (folder instanceof UploadedFolder)
            return (UploadedFolder) folder;
        else
            throw new IllegalArgumentException("Path: \""+ path + "\" is not a Folder!");
    }

    public UploadedFile validatePathThenQueryAndCastToFile(String path) {

        FileSystemElement uploadedFile = fileService.findFSElementWithContainedFilesByPath_noChecks(path);

        if(uploadedFile == null)
            throw new IllegalArgumentException("No File exists with the following path: " + path);

        if (uploadedFile instanceof UploadedFile)
            return (UploadedFile) uploadedFile;
        else
            throw new IllegalArgumentException("Path: \""+ path + "\" is not a File!");
    }

    public FileSystemElement validatePathAndQueryFSE(String path) {

        FileSystemElement fse = fileService.findFSElementWithContainedFilesByPath_noChecks(path);

        if(fse == null)
            throw new IllegalArgumentException("No File exists with the following path: " + path);

        return fse;
    }

    public String verifyPathNotEmpty(String path) {
        if (Strings.isNullOrEmpty(path))
            throw new IllegalArgumentException("The following ath is invalid: " + path);

        return path;
    }

}
