package com.thomaster.ourcloud.services.request.save.file;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveFileRequestFactoryTest {

    @Mock
    private OCUserService userService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private SaveFileRequestFactory requestFactory = new SaveFileRequestFactory(userService, fileService);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_createSaveFileRequest_happyPath() {
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

        when(userService.getCurrentlyLoggedInUser()).thenReturn(Optional.of(ocUser));
        when(fileService.findFSElementWithContainedFilesByPath_noChecks(any())).thenReturn(folder);

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        SaveFileRequest saveFileRequest = requestFactory.createSaveFileRequest("Thomaster", file, true, "text/plain");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(1)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getValue()).isEqualTo("Thomaster");
        assertThat(saveFileRequest.getSize()).isEqualTo(10);
        assertThat(saveFileRequest.getFileExtension()).isEqualTo("txt");
        assertThat(saveFileRequest.getOriginalName()).isEqualTo("file.txt");
        assertThat(saveFileRequest.isShouldOverrideExistingFile()).isEqualTo(true);
        assertThat(saveFileRequest.getParentFolder()).isEqualTo(folder);
        assertThat(saveFileRequest.getParentFolderOwner()).isEqualTo(ocUser);
        assertThat(saveFileRequest.getMimeType()).isEqualTo("text/plain");
        assertThat(saveFileRequest.getInitiatingUser().get()).isEqualTo(ocUser);
    }

    @Test
    void test_createSaveFileRequest_userNotLoggedIn() {
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

        when(userService.getCurrentlyLoggedInUser()).thenReturn(Optional.empty());
        when(fileService.findFSElementWithContainedFilesByPath_noChecks(any())).thenReturn(folder);

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        SaveFileRequest saveFileRequest = requestFactory.createSaveFileRequest("Thomaster", file, true, "text/plain");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(1)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getValue()).isEqualTo("Thomaster");
        assertThat(saveFileRequest.getSize()).isEqualTo(10);
        assertThat(saveFileRequest.getFileExtension()).isEqualTo("txt");
        assertThat(saveFileRequest.getOriginalName()).isEqualTo("file.txt");
        assertThat(saveFileRequest.isShouldOverrideExistingFile()).isEqualTo(true);
        assertThat(saveFileRequest.getParentFolder()).isEqualTo(folder);
        assertThat(saveFileRequest.getParentFolderOwner()).isEqualTo(ocUser);
        assertThat(saveFileRequest.getInitiatingUser()).isEmpty();
    }

    @Test
    void test_createSaveFileRequest_parentFolderNull() {
        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(Optional.of(ocUser));
        when(fileService.findFSElementWithContainedFilesByPath_noChecks("Thomaster")).thenReturn(null);

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        RequestValidationException requestValidationException = catchThrowableOfType(() -> requestFactory.createSaveFileRequest("Thomaster", file, true, "text/plain"), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_FSE_FOUND_CODE);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(1)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getValue()).isEqualTo("Thomaster");
    }

    @Test
    void test_createSaveFileRequest_parentFolderNotFolder() {
        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setParentFolderPath("Thomaster");
        uploadedFile.setFileSize(100L);
        uploadedFile.setOriginalName("file_that_is_not_folder.txt");
        uploadedFile.setRelativePath("Thomaster.file_that_is_not_folder_txt");
        uploadedFile.setOwner(ocUser);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(Optional.of(ocUser));
        when(fileService.findFSElementWithContainedFilesByPath_noChecks(any())).thenReturn(uploadedFile);

        MockMultipartFile file = new MockMultipartFile("TESTFILE", "file.txt","txt", new byte[10]);

        RequestValidationException requestValidationException = catchThrowableOfType(() -> requestFactory.createSaveFileRequest("Thomaster.file_that_is_not_folder_txt", file, true, "text/plain"), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_FSE_FOUND_CODE);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(1)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getValue()).isEqualTo("Thomaster.file_that_is_not_folder_txt");
    }
}