package com.thomaster.ourcloud.services.request.save.folder;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveFolderRequestFactoryTest {

    @Mock
    private OCUserService userService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private SaveFolderRequestFactory requestFactory = new SaveFolderRequestFactory(userService, fileService);

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_createSaveFolderRequest_happyPath() {
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

        SaveFolderRequest saveFolderRequest = requestFactory.createSaveFolderRequest("Thomaster", "New Folder", true);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(1)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getValue()).isEqualTo("Thomaster");
        assertThat(saveFolderRequest.getOriginalName()).isEqualTo("New Folder");
        assertThat(saveFolderRequest.isShouldOverrideExistingFile()).isEqualTo(true);
        assertThat(saveFolderRequest.getParentFolder()).isEqualTo(folder);
        assertThat(saveFolderRequest.getParentFolderOwner()).isEqualTo(ocUser);
        assertThat(saveFolderRequest.getInitiatingUser().get()).isEqualTo(ocUser);
    }

    @Test
    void test_createSaveFolderRequest_userNotLoggedIn() {
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

        SaveFolderRequest saveFolderRequest = requestFactory.createSaveFolderRequest("Thomaster", "New Folder", true);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(1)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getValue()).isEqualTo("Thomaster");
        assertThat(saveFolderRequest.getOriginalName()).isEqualTo("New Folder");
        assertThat(saveFolderRequest.isShouldOverrideExistingFile()).isEqualTo(true);
        assertThat(saveFolderRequest.getParentFolder()).isEqualTo(folder);
        assertThat(saveFolderRequest.getParentFolderOwner()).isEqualTo(ocUser);
        assertThat(saveFolderRequest.getInitiatingUser()).isEmpty();
    }

    @Test
    void test_createSaveFolderRequest_parentFolderNotExists() {
        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(Optional.of(ocUser));
        when(fileService.findFSElementWithContainedFilesByPath_noChecks(any())).thenReturn(null);

        RequestValidationException requestValidationException = catchThrowableOfType(() -> requestFactory.createSaveFolderRequest("Thomaster", "New Folder", true), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_FSE_FOUND_CODE);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(1)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();
    }

    @Test
    void test_createSaveFolderRequest_parentFolderNotFolder() {
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

        RequestValidationException requestValidationException = catchThrowableOfType(() -> requestFactory.createSaveFolderRequest("Thomaster.file_that_is_not_folder_txt", "New Folder", true), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_FSE_FOUND_CODE);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(1)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();
    }
}