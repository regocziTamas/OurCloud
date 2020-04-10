package com.thomaster.ourcloud.services.request.read;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ReadRequestValidatorTest {

    @Test
    public void test_userHasReadRight() {

        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setParentFolderPath("Thomaster");
        uploadedFile.setFileSize(100L);
        uploadedFile.setOriginalName("file_to_read.txt");
        uploadedFile.setRelativePath("Thomaster.file_to_read_txt");
        uploadedFile.setOwner(ocUser);

        ReadRequest readRequest = new ReadRequest.ReadRequestBuilder()
                .setFileToRead(uploadedFile)
                .setFileToReadOwner(uploadedFile.getOwner())
                .initiatingUser(ocUser)
                .build();

        ReadRequestValidator readRequestValidator = new ReadRequestValidator();

        assertThatCode(() -> readRequestValidator.validateRequest(readRequest)).doesNotThrowAnyException();
    }

    @Test
    public void test_userHasNoReadRight() {

        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        OCUser ocUser2 = new OCUser();
        ocUser2.setId(2L);
        ocUser2.setUsername("NotThomaster");
        ocUser2.setUsedBytes(120L);

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setParentFolderPath("Thomaster");
        uploadedFile.setFileSize(100L);
        uploadedFile.setOriginalName("file_to_read.txt");
        uploadedFile.setRelativePath("Thomaster.file_to_read_txt");
        uploadedFile.setOwner(ocUser);

        ReadRequest readRequest = new ReadRequest.ReadRequestBuilder()
                .setFileToRead(uploadedFile)
                .setFileToReadOwner(uploadedFile.getOwner())
                .initiatingUser(ocUser2)
                .build();

        ReadRequestValidator readRequestValidator = new ReadRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> readRequestValidator.validateRequest(readRequest), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_READ_ACCESS_CODE);
    }
}