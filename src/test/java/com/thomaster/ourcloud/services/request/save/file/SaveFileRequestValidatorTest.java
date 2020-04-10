package com.thomaster.ourcloud.services.request.save.file;

import com.google.common.io.Files;
import com.thomaster.ourcloud.model.filesystem.ContainedFSEInfo;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.*;

class SaveFileRequestValidatorTest {

    private OCUser ocUser;
    private OCUser ocUserTwo;
    private UploadedFolder folder;

    public SaveFileRequestValidatorTest() {
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

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        SaveFileRequest request = new SaveFileRequest.SaveFileRequestBuilder()
                .file(file)
                .size(file.getSize())
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        SaveFileRequestValidator validator = new SaveFileRequestValidator();

        assertThatCode(() -> validator.validateRequest(request)).doesNotThrowAnyException();
    }

    @Test
    public void test_noWriteRightToFolder() {

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        SaveFileRequest request = new SaveFileRequest.SaveFileRequestBuilder()
                .file(file)
                .size(file.getSize())
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUserTwo)
                .build();

        SaveFileRequestValidator validator = new SaveFileRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> validator.validateRequest(request), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_WRITE_PERM_CODE);
    }

    @Test
    public void test_userIsNotLoggedIn() {

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        SaveFileRequest request = new SaveFileRequest.SaveFileRequestBuilder()
                .file(file)
                .size(file.getSize())
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(null)
                .build();

        SaveFileRequestValidator validator = new SaveFileRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> validator.validateRequest(request), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NOT_LOGGED_IN_CODE);
    }

    @Test
    public void test_userHasNotEnoughStorageLeft() {

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        SaveFileRequest request = new SaveFileRequest.SaveFileRequestBuilder()
                .file(file)
                .size(1000000000000L)
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        SaveFileRequestValidator validator = new SaveFileRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> validator.validateRequest(request), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_MORE_STORAGE_CODE);
    }

    @Test
    public void test_forbiddenFileExtension() {

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.sh","sh", new byte[10]);

        SaveFileRequest request = new SaveFileRequest.SaveFileRequestBuilder()
                .file(file)
                .size(100L)
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        SaveFileRequestValidator validator = new SaveFileRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> validator.validateRequest(request), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.FORBIDDEN_EXT_CODE);
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
        fseInfo.setFolder(false);
        fseInfo.setOriginalName("file.txt");
        fseInfo.setParentFolderPath("Thomaster");
        fseInfo.setRelativePath("Thomaster.file_txt");
        fseInfo.setSize(120L);
        fseInfo.setId(2L);
        fseInfo.setOwner(ocUser);

        folder.getContainedFileInfos().add(fseInfo);

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        SaveFileRequest request = new SaveFileRequest.SaveFileRequestBuilder()
                .file(file)
                .size(100L)
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(false)
                .parentFolder(folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        SaveFileRequestValidator validator = new SaveFileRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> validator.validateRequest(request), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.FILENAME_NOT_UNIQUE_CODE);
    }

    @Test
    public void test_fileNameIsNotUniqueInFolder_shouldOverrideExistingFile_true() {

        UploadedFolder folder = new UploadedFolder();
        folder.setId(1L);
        folder.setParentFolderPath("");
        folder.setFileSize(100L);
        folder.setOriginalName("Thomaster");
        folder.setRelativePath("Thomaster");
        folder.setOwner(ocUser);

        ContainedFSEInfo fseInfo = new ContainedFSEInfo();
        fseInfo.setFolder(false);
        fseInfo.setOriginalName("file.txt");
        fseInfo.setParentFolderPath("Thomaster");
        fseInfo.setRelativePath("Thomaster.file_txt");
        fseInfo.setSize(120L);
        fseInfo.setId(2L);
        fseInfo.setOwner(ocUser);

        folder.getContainedFileInfos().add(fseInfo);

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        SaveFileRequest request = new SaveFileRequest.SaveFileRequestBuilder()
                .file(file)
                .size(100L)
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        SaveFileRequestValidator validator = new SaveFileRequestValidator();

        assertThatCode(() -> validator.validateRequest(request)).doesNotThrowAnyException();
    }

}