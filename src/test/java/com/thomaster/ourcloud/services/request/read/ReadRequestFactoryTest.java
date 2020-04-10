package com.thomaster.ourcloud.services.request.read;

import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadRequestFactoryTest {

    @Mock
    private OCUserService userService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private ReadRequestFactory requestFactory = new ReadRequestFactory(userService, fileService);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_createReadRequest_happyPath() {
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

        ReadRequest readRequest = requestFactory.createReadRequest(folder);

        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(readRequest.getFileToRead()).isEqualTo(folder);
        assertThat(readRequest.getFileToReadOwner()).isEqualTo(ocUser);
        assertThat(readRequest.getInitiatingUser().get()).isEqualTo(ocUser);
    }

    @Test
    void test_createReadRequest_userNotLoggedIn() {
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

        ReadRequest readRequest = requestFactory.createReadRequest(folder);

        verify(userService, times(1)).getCurrentlyLoggedInUser();

        assertThat(readRequest.getFileToRead()).isEqualTo(folder);
        assertThat(readRequest.getFileToReadOwner()).isEqualTo(ocUser);
        assertThat(readRequest.getInitiatingUser()).isEmpty();
    }
}