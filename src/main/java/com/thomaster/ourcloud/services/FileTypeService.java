package com.thomaster.ourcloud.services;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Component
public class FileTypeService {

    private Tika tika;

    public FileTypeService() {
        this.tika = new Tika();
    }

    public String determineMIMEtype(MultipartFile fileToUpload)
    {
        String fileType = fileToUpload.getContentType();

        if (fileType == null || fileType.equals("application/octet-stream"))
        {
            String tikaDetectionResult = null;

            try {
                tikaDetectionResult = tika.detect(fileToUpload.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Objects.requireNonNullElse(tikaDetectionResult, "application/octet-stream");
        } else {
            return fileType;
        }
    }

}
