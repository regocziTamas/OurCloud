package com.thomaster.ourcloud.services.request.delete;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DeleteRequestValidatorTest {

    @Test
    public void test_happyPath() {

        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        UploadedFolder folder = new UploadedFolder();
        folder.setParentFolderPath("");
        folder.setFileSize(100L);
        folder.setOriginalName("Thomaster");
        folder.setRelativePath("Thomaster");
        folder.setOwner(ocUser);

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setParentFolderPath("Thomaster");
        uploadedFile.setFileSize(100L);
        uploadedFile.setOriginalName("file_to_delete.txt");
        uploadedFile.setRelativePath("Thomaster.file_to_delete_txt");
        uploadedFile.setOwner(ocUser);

        DeleteRequest request = new DeleteRequest.DeleteRequestBuilder()
                .fileToDelete(uploadedFile)
                .fileToDeleteOwner(ocUser)
                .parentFolder(folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        DeleteRequestValidator validator = new DeleteRequestValidator();

        assertThatCode(() -> validator.validateRequest(request)).doesNotThrowAnyException();
    }

    @Test
    public void test_noWriteRightToFolder() {

        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        OCUser ocUserTwo = new OCUser();
        ocUserTwo.setId(2L);
        ocUserTwo.setUsername("NotThomaster");
        ocUserTwo.setUsedBytes(100L);

        UploadedFolder folder = new UploadedFolder();
        folder.setParentFolderPath("");
        folder.setFileSize(100L);
        folder.setOriginalName("Thomaster");
        folder.setRelativePath("Thomaster");
        folder.setOwner(ocUser);

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setParentFolderPath("Thomaster");
        uploadedFile.setFileSize(100L);
        uploadedFile.setOriginalName("file_to_delete.txt");
        uploadedFile.setRelativePath("Thomaster.file_to_delete_txt");
        uploadedFile.setOwner(ocUser);

        DeleteRequest request = new DeleteRequest.DeleteRequestBuilder()
                .fileToDelete(uploadedFile)
                .fileToDeleteOwner(ocUser)
                .parentFolder(folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUserTwo)
                .build();

        DeleteRequestValidator validator = new DeleteRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> validator.validateRequest(request), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_WRITE_PERM_CODE);
    }
}