package com.thomaster.ourcloud.controllers;

import com.thomaster.ourcloud.json.FileSystemElementJSON;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.FileSystemService;
import com.thomaster.ourcloud.services.FileTypeService;
import com.thomaster.ourcloud.services.request.delete.DeleteRequest;
import com.thomaster.ourcloud.services.request.delete.DeleteRequestFactory;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequest;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequestFactory;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequest;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequestFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

@RestController
public class FileController {

    private FileService fileService;
    private FileSystemService fileSystemService;
    private SaveFileRequestFactory saveFileRequestFactory;
    private SaveFolderRequestFactory saveFolderRequestFactory;
    private DeleteRequestFactory deleteRequestFactory;
    private FileTypeService fileTypeService;

    public FileController(FileService fileService,
                          FileSystemService fileSystemService,
                          SaveFileRequestFactory saveFileRequestFactory,
                          SaveFolderRequestFactory saveFolderRequestFactory,
                          DeleteRequestFactory deleteRequestFactory,
                          FileTypeService fileTypeService) {
        this.fileService = fileService;
        this.fileSystemService = fileSystemService;
        this.saveFileRequestFactory = saveFileRequestFactory;
        this.saveFolderRequestFactory = saveFolderRequestFactory;
        this.deleteRequestFactory = deleteRequestFactory;
        this.fileTypeService = fileTypeService;
    }

    @GetMapping("/files")
    public String fileController()
    {
        return "{\"msg\": \"Msg from backend\"}";
    }

    @GetMapping("/file")
    public FileSystemElementJSON getFile(@RequestParam("fileToPathToGet") String fileToPathToGet) {
        FileSystemElement fileSystemElement = fileService.findFSElementWithContainedFilesByPath(fileToPathToGet.replace("/", "."));

        return FileSystemElementJSON.of(fileSystemElement);
    }

    @PostMapping("/upload/file")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("parentFolderPath") String parentFolderPath,
                             @RequestParam("shouldOverrideExistingFile") boolean shouldOverrideExistingFile) {
        System.out.println("AAAAAAAAAAAAAAAAAa:" + file.getContentType());
        String mimeType = fileTypeService.determineMIMEtype(file);

        SaveFileRequest saveFileRequest = saveFileRequestFactory.createSaveFileRequest(parentFolderPath, file, shouldOverrideExistingFile, mimeType);
        fileService.saveFile(saveFileRequest);
        return "{\"result\": \"OK\"}";
    }

    @PostMapping("/upload/folder")
    public String uploadFolder(@RequestParam("parentFolderPath") String parentFolderPath,
                             @RequestParam("newFolderName") String newFolderName,
                             @RequestParam("shouldOverrideExistingFile") boolean shouldOverrideExistingFile) {
        SaveFolderRequest saveFolderRequest = saveFolderRequestFactory.createSaveFolderRequest(parentFolderPath, newFolderName, shouldOverrideExistingFile);
        fileService.saveFolder(saveFolderRequest);
        return "{\"result\": \"OK\"}";
    }

    @DeleteMapping("/delete/fse")
    public String deleteFile(@RequestParam("fileToPathToDelete") String fileToPathToDelete) {
        DeleteRequest deleteRequest = deleteRequestFactory.createDeleteRequest(fileToPathToDelete);
        fileService.deleteFSElementRecursively(deleteRequest);
        return "{\"result\": \"OK\"}";
    }

    @GetMapping(value= "/download")
    public void downloadFile(@RequestParam("pathToFileToDownload") String pathToFileToDownload, HttpServletResponse response) throws FileNotFoundException {
        UploadedFile fileToDownload = (UploadedFile) fileService.queryFileToDownload(pathToFileToDownload);
        fileSystemService.addDownloadFileToResponse(fileToDownload, response);

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/x-bittorrent");
//        //headers.add("Content-Disposition", "attachment; filename=" + fileToDownload.getOriginalName());
//
//        return new ResponseEntity<String>("kabbe", headers, HttpStatus.OK);
    }

}
