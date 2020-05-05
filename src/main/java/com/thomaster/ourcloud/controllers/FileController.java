package com.thomaster.ourcloud.controllers;

import com.thomaster.ourcloud.json.FileSystemElementJSON;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.FileSystemService;
import com.thomaster.ourcloud.services.FileTypeService;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import com.thomaster.ourcloud.services.request.delete.DeleteRequest;
import com.thomaster.ourcloud.services.request.delete.DeleteRequestFactory;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequest;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequestFactory;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequest;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequestFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

import static com.thomaster.ourcloud.Constants.API_ENDPOINT_PREFIX;

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

    @GetMapping(API_ENDPOINT_PREFIX +"/files")
    public String fileController()
    {
        return "{\"msg\": \"Msg from backend\"}";
    }

    @GetMapping(API_ENDPOINT_PREFIX +"/file")
    public ResponseEntity<FileSystemElementJSON> getFile(@RequestParam("fileToPathToGet") String fileToPathToGet) {
        FileSystemElement fileSystemElement = fileService.findFSElementWithContainedFilesByPath(fileToPathToGet.replace("/", "."));

        return ResponseEntity.ok()
                .body(FileSystemElementJSON.of(fileSystemElement));


//        return FileSystemElementJSON.of(fileSystemElement);
    }

    @PostMapping(API_ENDPOINT_PREFIX +"/upload/file")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("parentFolderPath") String parentFolderPath,
                             @RequestParam("shouldOverrideExistingFile") boolean shouldOverrideExistingFile) {

        String mimeType = fileTypeService.determineMIMEtype(file);

        SaveFileRequest saveFileRequest = saveFileRequestFactory.createSaveFileRequest(parentFolderPath, file, shouldOverrideExistingFile, mimeType);

        fileService.saveFile(saveFileRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping(API_ENDPOINT_PREFIX +"/upload/folder")
    public ResponseEntity<Object> uploadFolder(@RequestParam("parentFolderPath") String parentFolderPath,
                             @RequestParam("newFolderName") String newFolderName,
                             @RequestParam("shouldOverrideExistingFile") boolean shouldOverrideExistingFile) {

        SaveFolderRequest saveFolderRequest = saveFolderRequestFactory.createSaveFolderRequest(parentFolderPath, newFolderName, shouldOverrideExistingFile);

        fileService.saveFolder(saveFolderRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(API_ENDPOINT_PREFIX +"/delete/fse")
    public ResponseEntity<Object> deleteFile(@RequestParam("fileToPathToDelete") String fileToPathToDelete) {

        DeleteRequest deleteRequest = deleteRequestFactory.createDeleteRequest(fileToPathToDelete);

        fileService.deleteFSElementRecursively(deleteRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value= API_ENDPOINT_PREFIX +"/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("pathToFileToDownload") String pathToFileToDownload) {

        UploadedFile fileToDownload = (UploadedFile) fileService.queryFileToDownload(pathToFileToDownload);
        InputStreamResource inputStream = fileSystemService.getFileAsInputStream(fileToDownload);

        return ResponseEntity.ok()
                .contentLength(fileToDownload.getFileSize())
                .contentType(MediaType.parseMediaType(fileToDownload.getMimeType()))
                .header("Content-Disposition", "attachment; filename=" + fileToDownload.getOriginalName())
                .body(inputStream);
    }

    @ExceptionHandler({RequestValidationException.class})
    public ResponseEntity<String> handleFileControllerException(Exception ex, WebRequest request) {
        RequestValidationException reqValException = (RequestValidationException) ex;

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(reqValException.getErrorMsg());
    }

}
