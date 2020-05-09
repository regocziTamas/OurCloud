package com.thomaster.ourcloud.services;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
public class FileSystemService {

    private static final String STORAGE_PATH = System.getenv("STORAGE_PATH");

    public void writeFile(MultipartFile fileToSave, String filenameOnDisk) {

        try {
            FileOutputStream outputStream = new FileOutputStream(STORAGE_PATH + "/" + filenameOnDisk);
            outputStream.write(fileToSave.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public InputStreamResource getFileAsInputStream(UploadedFile fileToDownload) {
        String absFilePath = STORAGE_PATH + "/" + fileToDownload.getFilenameOnDisk();

        try {
            return new InputStreamResource(new FileInputStream(absFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw RequestValidationException.noFileSystemElementFound(fileToDownload.getRelativePath(), "File");
        }
    }


    public void deleteFilesOnDisk(Set<String> filenames) {
        filenames.stream()
                .map(filename -> STORAGE_PATH + "/" + filename)
                .forEach(filename -> {
                    try {
                        Files.delete(Path.of(filename));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
