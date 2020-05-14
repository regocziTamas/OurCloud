package com.thomaster.ourcloud.services.request.save.file;

import com.google.common.io.Files;
import com.thomaster.ourcloud.model.filesystem.ContainedFSEInfo;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.DigestUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class PreFlightSaveFileRequestValidatorTest {

    private OCUser ocUser;
    private OCUser ocUserTwo;
    private UploadedFolder folder;

    public PreFlightSaveFileRequestValidatorTest() {
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

        PreFlightSaveFileRequest request = new PreFlightSaveFileRequest.PreFlightSaveFileRequestBuilder()
                //.file(file)
                .size(file.getSize())
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        PreFlightSaveFileRequestValidator validator = new PreFlightSaveFileRequestValidator();

        assertThatCode(() -> validator.validateRequest(request)).doesNotThrowAnyException();
    }

    @Test
    public void test_noWriteRightToFolder() {

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        PreFlightSaveFileRequest request = new PreFlightSaveFileRequest.PreFlightSaveFileRequestBuilder()
                //.file(file)
                .size(file.getSize())
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUserTwo)
                .build();

        PreFlightSaveFileRequestValidator validator = new PreFlightSaveFileRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> validator.validateRequest(request), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_WRITE_PERM_CODE);
    }

    @Test
    public void test_userIsNotLoggedIn() {

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        PreFlightSaveFileRequest request = new PreFlightSaveFileRequest.PreFlightSaveFileRequestBuilder()
                //.file(file)
                .size(file.getSize())
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(null)
                .build();

        PreFlightSaveFileRequestValidator validator = new PreFlightSaveFileRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> validator.validateRequest(request), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NOT_LOGGED_IN_CODE);
    }

    @Test
    public void test_userHasNotEnoughStorageLeft() {

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        PreFlightSaveFileRequest request = new PreFlightSaveFileRequest.PreFlightSaveFileRequestBuilder()
                //.file(file)
                .size(1000000000000L)
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        PreFlightSaveFileRequestValidator validator = new PreFlightSaveFileRequestValidator();

        RequestValidationException requestValidationException = catchThrowableOfType(() -> validator.validateRequest(request), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_MORE_STORAGE_CODE);
    }

    @Test
    public void test_forbiddenFileExtension() {

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.sh","sh", new byte[10]);

        PreFlightSaveFileRequest request = new PreFlightSaveFileRequest.PreFlightSaveFileRequestBuilder()
                //.file(file)
                .size(100L)
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(this.folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        PreFlightSaveFileRequestValidator validator = new PreFlightSaveFileRequestValidator();

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

        PreFlightSaveFileRequest request = new PreFlightSaveFileRequest.PreFlightSaveFileRequestBuilder()
                //.file(file)
                .size(100L)
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(false)
                .parentFolder(folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        PreFlightSaveFileRequestValidator validator = new PreFlightSaveFileRequestValidator();

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

        PreFlightSaveFileRequest request = new PreFlightSaveFileRequest.PreFlightSaveFileRequestBuilder()
                //.file(file)
                .size(100L)
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(true)
                .parentFolder(folder)
                .parentFolderOwner(ocUser)
                .initiatingUser(ocUser)
                .build();

        PreFlightSaveFileRequestValidator validator = new PreFlightSaveFileRequestValidator();

        assertThatCode(() -> validator.validateRequest(request)).doesNotThrowAnyException();
    }

    @Test
    public void test() throws IOException, NoSuchAlgorithmException {

        /**
         * for frontend https://www.npmjs.com/package/ts-md5
         * https://stackoverflow.com/questions/4543018/generate-md5-hash-for-a-list-of-integers
         */

        String filename = "shrek.jpg";

        FileInputStream fileInputStream = new FileInputStream(filename);

        byte[] bytes = fileInputStream.readAllBytes();

//        byte[] signed = fileInputStream.readAllBytes();
//        int[] unsigned = new int[signed.length];
//        for (int i = 0; i < signed.length; i++) {
//            unsigned[i] = signed[i] & 0xFF;
//        }
//        ByteBuffer buf = ByteBuffer.allocate(4*unsigned.length);
//        buf.order(ByteOrder.LITTLE_ENDIAN);
//        for (int i = 0; i < unsigned.length; ++i)
//            buf.putInt(unsigned[i]);
//        byte[] barr = buf.array();

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(filename.getBytes());
        md.update(bytes);
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toLowerCase();

        System.out.println(myHash);
    }

}