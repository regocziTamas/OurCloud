package com.thomaster.ourcloud.services.request.save.folder;

import com.google.common.io.Files;
import com.thomaster.ourcloud.model.filesystem.ContainedFSEInfo;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequest;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequestValidator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class SaveFolderRequestValidatorTest {

    private OCUser ocUser;
    private OCUser ocUserTwo;
    private UploadedFolder folder;

    public SaveFolderRequestValidatorTest() {
        ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        ocUserTwo = new OCUser();
        ocUserTwo.setId(2L);
        ocUserTwo.setUsername("NotThomaster");
        ocUserTwo.setUsedBytes(100L);

        folder = new UploadedFolder();
        folder.setParentFolderPath("");
        folder.setFileSize(100L);
        folder.setOriginalName("Thomaster");
        folder.setRelativePath("Thomaster");
        folder.setOwner(ocUser);
    }

    @Test
    public void test_happyPath() {

        SaveFolderRequest request = new SaveFolderRequest.SaveFolderRequestBuilder()
                .originalName("Folder_02")
                .shouldOverrideExistingFile(false)
                .parentFolder(folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        SaveFolderRequestValidator validator = new SaveFolderRequestValidator();

        assertThatCode(() -> validator.validateRequest(request)).doesNotThrowAnyException();
    }

    @Test
    public void test_userHasNoWriteAccess() {

        SaveFolderRequest request = new SaveFolderRequest.SaveFolderRequestBuilder()
                .originalName("Folder_02")
                .shouldOverrideExistingFile(false)
                .parentFolder(folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUserTwo)
                .build();

        SaveFolderRequestValidator validator = new SaveFolderRequestValidator();

        assertThatThrownBy(() -> validator.validateRequest(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void test_fileNameIsNotUniqueInFolder() {

        UploadedFolder folder = new UploadedFolder();
        folder.setId(1L);
        folder.setParentFolderPath("");
        folder.setFileSize(100L);
        folder.setOriginalName("Thomaster");
        folder.setRelativePath("Thomaster");
        folder.setOwner(ocUser);

        ContainedFSEInfo fseInfo = new ContainedFSEInfo();
        fseInfo.setFolder(true);
        fseInfo.setOriginalName("Folder_01");
        fseInfo.setParentFolderPath("Thomaster");
        fseInfo.setRelativePath("Thomaster.Folder_01");
        fseInfo.setSize(120L);
        fseInfo.setId(2L);
        fseInfo.setOwner(ocUser);

        folder.getContainedFileInfos().add(fseInfo);

        SaveFolderRequest request = new SaveFolderRequest.SaveFolderRequestBuilder()
                .originalName("Folder_01")
                .shouldOverrideExistingFile(false)
                .parentFolder(folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        SaveFolderRequestValidator validator = new SaveFolderRequestValidator();

        assertThatThrownBy(() -> validator.validateRequest(request)).isInstanceOf(IllegalArgumentException.class);
    }
}