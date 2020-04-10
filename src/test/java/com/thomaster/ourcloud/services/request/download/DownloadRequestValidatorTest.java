package com.thomaster.ourcloud.services.request.download;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import com.thomaster.ourcloud.services.request.read.ReadRequest;
import com.thomaster.ourcloud.services.request.read.ReadRequestValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class DownloadRequestValidatorTest {
    @Test
    public void test_elementToDownloadIsNotFile() {

        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        UploadedFolder folder = new UploadedFolder();
        folder.setId(1L);
        folder.setParentFolderPath("");
        folder.setFileSize(100L);
        folder.setOriginalName("Thomaster");
        folder.setRelativePath("Thomaster");
        folder.setOwner(ocUser);

        ReadRequest readRequest = new ReadRequest.ReadRequestBuilder()
                .setFileToRead(folder)
                .setFileToReadOwner(folder.getOwner())
                .initiatingUser(ocUser)
                .build();

        DownloadRequestValidator downloadRequestValidator = new DownloadRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> downloadRequestValidator.validateRequest(readRequest), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NOT_A_FILE_DOWNLOAD_CODE);
    }
}