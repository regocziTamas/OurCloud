package com.thomaster.ourcloud.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
public class FileSystemService {

    FilePathService filePathService;

    public FileSystemService(FilePathService filePathService) {
        this.filePathService = filePathService;
    }

    public void writeFile(String absolutePath, MultipartFile fileToSave) {
        try {
            FileOutputStream fos = new FileOutputStream(absolutePath);
            fos.write(fileToSave.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(MultipartFile fileToSave) {
        System.out.println("========= File writing not working, should be implemented! =========");
    }

    public void deleteRecursively(String absolutePath) {
        try {
            Files.walk(Path.of(absolutePath))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
