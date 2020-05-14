package com.thomaster.ourcloud.services.request.save.file;

import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.FileTypeService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.PreFlightSaveFileRequestTokenService;
import com.thomaster.ourcloud.services.request.base.BaseRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class SaveFileRequestFactory extends BaseRequestFactory<SaveFileRequest> {

    private PreFlightSaveFileRequestTokenService tokenService;
    private FileTypeService fileTypeService;

    public SaveFileRequestFactory(OCUserService userService, FileService fileService, PreFlightSaveFileRequestTokenService tokenService, FileTypeService fileTypeService) {
        super(userService, fileService);
        this.tokenService = tokenService;
        this.fileTypeService = fileTypeService;
    }

    public SaveFileRequest createSaveFileRequest(MultipartFile fileToUpload, String uploadToken) {

        String mimeType = fileTypeService.determineMIMEtype(fileToUpload);

        OCUser initiatingUser = userService.getCurrentlyLoggedInUser().orElse(null);
        PreFlightSaveFileRequest preFlightSaveFileRequest = tokenService.getRequestForToken(uploadToken);

        return new SaveFileRequest.SaveFileRequestBuilder()
                .mimeType(mimeType)
                .preFlightSaveFileRequest(preFlightSaveFileRequest)
                .md5Hash(getMD5Hash(fileToUpload))
                .fileToUpload(fileToUpload)
                .uploadToken(uploadToken)
                .initiatingUser(initiatingUser)
                .build();
    }

    private String getMD5Hash(MultipartFile fileToUpload) {
        MessageDigest md = getMessageDigest();
        md.update(getOriginalFilenameOrEmptyString(fileToUpload).getBytes());
        md.update(readBytesOfFileToUpload(fileToUpload));

        return DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
    }

    private byte[] readBytesOfFileToUpload(MultipartFile fileToUpload)  {
        try {
            return fileToUpload.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not read bytes from the file");
        }
    }

    private String getOriginalFilenameOrEmptyString(MultipartFile fileToUpload) {
        String originalFilename = fileToUpload.getOriginalFilename();
        return originalFilename == null ? "" : originalFilename;
    }

    private MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not get MD5 MessageDigest!");
        }
    }
}
