package com.thomaster.ourcloud.services;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
public class FileSystemService {

    FilePathService filePathService;
    private static final String STORAGE_PATH = "/home/thomaster/ourcloud_userfiles";

    public FileSystemService(FilePathService filePathService) {
        this.filePathService = filePathService;
    }



    public void writeFile(MultipartFile fileToSave, String filenameOnDisk) {

        try {
            FileOutputStream outputStream = new FileOutputStream(STORAGE_PATH + "/" + filenameOnDisk);
            outputStream.write(fileToSave.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDownloadFileToResponse(UploadedFile fileToDownload, HttpServletResponse response) {

        try {
            FileInputStream inputStream = new FileInputStream(STORAGE_PATH + "/" + fileToDownload.getFilenameOnDisk());
            response.getOutputStream().write(inputStream.readAllBytes());

            String fileName = URLEncoder.encode(fileToDownload.getOriginalName(), StandardCharsets.UTF_8);

            fileName = URLDecoder.decode(fileName, StandardCharsets.ISO_8859_1);

            response.setContentType(fileToDownload.getMimeType());
            response.setHeader("Content-Disposition", "attachment; filename=valami.txt");

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Some IO exception happened!");
        }
    }

    public File readFile(UploadedFile fileToDownload) {
        return new File(STORAGE_PATH + "/" + fileToDownload.getFilenameOnDisk());
    }
}
