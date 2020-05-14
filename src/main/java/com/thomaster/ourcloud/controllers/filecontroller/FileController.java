package com.thomaster.ourcloud.controllers.filecontroller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomaster.ourcloud.json.FileSystemElementJSON;
import com.thomaster.ourcloud.json.UploadTokenJSON;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.FileSystemService;
import com.thomaster.ourcloud.services.FileTypeService;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import com.thomaster.ourcloud.services.request.delete.DeleteRequest;
import com.thomaster.ourcloud.services.request.delete.DeleteRequestFactory;
import com.thomaster.ourcloud.services.request.save.file.PreFlightSaveFileRequest;
import com.thomaster.ourcloud.services.request.save.file.PreFlightSaveFileRequestFactory;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequest;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequestFactory;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequest;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequestFactory;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.mediatype.JacksonHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;

import static com.thomaster.ourcloud.Constants.API_ENDPOINT_PREFIX;

@RestController
public class FileController {

    private FileService fileService;
    private FileSystemService fileSystemService;
    private PreFlightSaveFileRequestFactory preFlightSaveFileRequestFactory;
    private SaveFileRequestFactory saveFileRequestFactory;
    private SaveFolderRequestFactory saveFolderRequestFactory;
    private DeleteRequestFactory deleteRequestFactory;

    public FileController(FileService fileService,
                          FileSystemService fileSystemService,
                          PreFlightSaveFileRequestFactory preFlightSaveFileRequestFactory,
                          SaveFileRequestFactory saveFileRequestFactory, SaveFolderRequestFactory saveFolderRequestFactory,
                          DeleteRequestFactory deleteRequestFactory) {
        this.fileService = fileService;
        this.fileSystemService = fileSystemService;
        this.preFlightSaveFileRequestFactory = preFlightSaveFileRequestFactory;
        this.saveFileRequestFactory = saveFileRequestFactory;
        this.saveFolderRequestFactory = saveFolderRequestFactory;
        this.deleteRequestFactory = deleteRequestFactory;
    }

    @GetMapping(API_ENDPOINT_PREFIX +"/file")
    public ResponseEntity<FileSystemElementJSON> getFile(@RequestParam("fileToPathToGet") String fileToPathToGet) {

        FileSystemElement fileSystemElement = fileService.findFSElementWithContainedFilesByPath(fileToPathToGet.replace("/", "."));

        return ResponseEntity.ok()
                .body(FileSystemElementJSON.of(fileSystemElement));
    }

    @PostMapping(API_ENDPOINT_PREFIX +"/upload/preflight/file")
    public ResponseEntity<UploadTokenJSON> registerPreflightSaveRequest(
                             @RequestParam("hash") String hash,
                             @RequestParam("originalName") String originalName,
                             @RequestParam("size") long size,
                             @RequestParam("parentFolderPath") String parentFolderPath,
                             @RequestParam("shouldOverrideExistingFile") boolean shouldOverrideExistingFile) {

        PreFlightSaveFileRequest preFlightSaveFileRequest = preFlightSaveFileRequestFactory.createPreFlightSaveFileRequest(parentFolderPath, hash, originalName, size, shouldOverrideExistingFile);

        String uploadToken = fileService.registerPreFlightSaveFileRequest(preFlightSaveFileRequest);

        return ResponseEntity
                .ok()
                .body(new UploadTokenJSON(uploadToken));
    }

    @PostMapping(API_ENDPOINT_PREFIX +"/upload/file")
    public ResponseEntity<Object> uploadFile(
                @RequestParam("file") MultipartFile fileToUpload,
                @RequestParam("uploadToken") String uploadToken) {

        SaveFileRequest saveFileRequest = saveFileRequestFactory.createSaveFileRequest(fileToUpload, uploadToken);

        fileService.saveFile(saveFileRequest);

        return ResponseEntity
                .ok()
                .build();
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


}
