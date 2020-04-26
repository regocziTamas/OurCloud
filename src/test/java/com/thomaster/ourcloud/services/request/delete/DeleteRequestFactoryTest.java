package com.thomaster.ourcloud.services.request.delete;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRequestFactoryTest {

    @Mock
    private OCUserService userService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private DeleteRequestFactory requestFactory = new DeleteRequestFactory(userService, fileService);

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_createDeleteRequest_happyPath() {
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

        when(userService.getCurrentlyLoggedInUser()).thenReturn(Optional.of(ocUser));
        when(fileService.findFSElementWithContainedFilesByPath_noChecks("Thomaster")).thenReturn(folder);
        when(fileService.findFSElementWithContainedFilesByPath_noChecks("Thomaster.file_to_delete_txt")).thenReturn(uploadedFile);

        DeleteRequest deleteRequest = requestFactory.createDeleteRequest("Thomaster.file_to_delete_txt");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(2)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getAllValues()).containsExactlyInAnyOrder("Thomaster", "Thomaster.file_to_delete_txt");
        assertThat(deleteRequest.getFileToDelete()).isEqualTo(uploadedFile);
        assertThat(deleteRequest.getFileToDeleteOwner()).isEqualTo(ocUser);
        assertThat(deleteRequest.getParentFolder()).isEqualTo(folder);
        assertThat(deleteRequest.getParentFolderOwner()).isEqualTo(ocUser);
        assertThat(deleteRequest.getInitiatingUser().get()).isEqualTo(ocUser);
    }

    @Test
    void test_createDeleteRequest_userNotLoggedIn() {
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

        when(userService.getCurrentlyLoggedInUser()).thenReturn(Optional.empty());
        when(fileService.findFSElementWithContainedFilesByPath_noChecks("Thomaster")).thenReturn(folder);
        when(fileService.findFSElementWithContainedFilesByPath_noChecks("Thomaster.file_to_delete_txt")).thenReturn(uploadedFile);

        DeleteRequest deleteRequest = requestFactory.createDeleteRequest("Thomaster.file_to_delete_txt");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(2)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getAllValues()).containsExactlyInAnyOrder("Thomaster", "Thomaster.file_to_delete_txt");
        assertThat(deleteRequest.getFileToDelete()).isEqualTo(uploadedFile);
        assertThat(deleteRequest.getFileToDeleteOwner()).isEqualTo(ocUser);
        assertThat(deleteRequest.getParentFolder()).isEqualTo(folder);
        assertThat(deleteRequest.getParentFolderOwner()).isEqualTo(ocUser);
        assertThat(deleteRequest.getInitiatingUser()).isEmpty();
    }

    @Test
    void test_fileToDeleteDoesNotExist() {
        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(Optional.of(ocUser));
        when(fileService.findFSElementWithContainedFilesByPath_noChecks("Thomaster.file_to_delete_txt")).thenReturn(null);

        RequestValidationException requestValidationException = catchThrowableOfType(() -> requestFactory.createDeleteRequest("Thomaster.file_to_delete_txt"), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_FSE_FOUND_CODE);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(1)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getValue()).isEqualTo("Thomaster.file_to_delete_txt");
    }

    @Test
    void test_parentFolderOfFileToDeleteDoesNotExist() {
        OCUser ocUser = new OCUser();
        ocUser.setId(1L);
        ocUser.setUsername("Thomaster");
        ocUser.setUsedBytes(100L);

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setParentFolderPath("Thomaster");
        uploadedFile.setFileSize(100L);
        uploadedFile.setOriginalName("file_to_delete.txt");
        uploadedFile.setRelativePath("Thomaster/file_to_delete_txt");
        uploadedFile.setOwner(ocUser);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(Optional.of(ocUser));
        when(fileService.findFSElementWithContainedFilesByPath_noChecks("Thomaster")).thenReturn(null);
        when(fileService.findFSElementWithContainedFilesByPath_noChecks("Thomaster.file_to_delete_txt")).thenReturn(uploadedFile);

        RequestValidationException requestValidationException = catchThrowableOfType(() -> requestFactory.createDeleteRequest("Thomaster.file_to_delete_txt"), RequestValidationException.class);
        assertThat(requestValidationException.getErrorCode()).isEqualTo(RequestValidationException.NO_FSE_FOUND_CODE);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(fileService, times(2)).findFSElementWithContainedFilesByPath_noChecks(argumentCaptor.capture());
        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(argumentCaptor.getAllValues()).containsExactlyInAnyOrder("Thomaster", "Thomaster.file_to_delete_txt");
    }
}