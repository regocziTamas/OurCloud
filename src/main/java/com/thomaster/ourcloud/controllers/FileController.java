package com.thomaster.ourcloud.controllers;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.request.delete.DeleteRequest;
import com.thomaster.ourcloud.services.request.delete.DeleteRequestFactory;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequest;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequestFactory;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequest;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    private FileService fileService;
    private SaveFileRequestFactory saveFileRequestFactory;
    private SaveFolderRequestFactory saveFolderRequestFactory;
    private DeleteRequestFactory deleteRequestFactory;

    public FileController(FileService fileService, SaveFileRequestFactory saveFileRequestFactory, SaveFolderRequestFactory saveFolderRequestFactory, DeleteRequestFactory deleteRequestFactory) {
        this.fileService = fileService;
        this.saveFileRequestFactory = saveFileRequestFactory;
        this.saveFolderRequestFactory = saveFolderRequestFactory;
        this.deleteRequestFactory = deleteRequestFactory;
    }

    @GetMapping("/files")
    public String fileController()
    {
        return "{\"msg\": \"Msg from backend\"}";
    }

    @GetMapping("/file")
    public String getFile(@RequestParam("fileToPathToGet") String fileToPathToGet)
    {
        FileSystemElement fileSystemElement = fileService.findFSElementWithContainedFilesByPath(fileToPathToGet.replace("/", "."));

        //return FileSystemElementJSON.of(fileSystemElement);
        return "Under construction";
    }

    @PostMapping("/upload/file")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("parentFolderPath") String parentFolderPath,
                             @RequestParam("shouldOverrideExistingFile") boolean shouldOverrideExistingFile)
    {
        SaveFileRequest saveFileRequest = saveFileRequestFactory.createSaveFileRequest(parentFolderPath, file, shouldOverrideExistingFile);
        fileService.saveFile(saveFileRequest);
        return "OK";
    }

    @GetMapping("/upload/folder")
    public String uploadFile(@RequestParam("parentFolderPath") String parentFolderPath,
                             @RequestParam("newFolderName") String newFolderName,
                             @RequestParam("shouldOverrideExistingFile") boolean shouldOverrideExistingFile)
    {
        SaveFolderRequest saveFolderRequest = saveFolderRequestFactory.createSaveFolderRequest(parentFolderPath, newFolderName, shouldOverrideExistingFile);
        fileService.saveFolder(saveFolderRequest);
        return "OK";
    }

    @GetMapping("/delete/folder")
    public String deleteFile(@RequestParam("fileToPathToGet") String fileToPathToDelete)
    {
        DeleteRequest deleteRequest = deleteRequestFactory.createDeleteRequest(fileToPathToDelete);
        fileService.deleteFSElementRecursively(deleteRequest);
        return "OK";
    }

}
