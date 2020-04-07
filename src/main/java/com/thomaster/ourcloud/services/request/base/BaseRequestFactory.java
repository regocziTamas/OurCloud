package com.thomaster.ourcloud.services.request.base;


import com.google.common.base.Strings;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.RequestValidationException;
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

        FileSystemElement folder = validatePathAndQueryFSE(path);

        if (folder instanceof UploadedFolder)
            return (UploadedFolder) folder;
        else
            throw RequestValidationException.noFileSystemElementFound(path, "Folder");
    }

    public UploadedFile validatePathThenQueryAndCastToFile(String path) {

        FileSystemElement uploadedFile = validatePathAndQueryFSE(path);

        if (uploadedFile instanceof UploadedFile)
            return (UploadedFile) uploadedFile;
        else
            throw RequestValidationException.noFileSystemElementFound(path, "File");
    }

    public FileSystemElement validatePathAndQueryFSE(String path) {

        FileSystemElement fse = fileService.findFSElementWithContainedFilesByPath_noChecks(path);

        if(fse == null)
            throw RequestValidationException.noFileSystemElementFound(path, "FileSystemElement (Folder or File)");

        return fse;
    }
}
