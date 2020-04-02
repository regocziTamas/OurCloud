package com.thomaster.ourcloud.services.request.delete;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.base.BaseRequestFactory;
import org.springframework.stereotype.Service;

@Service
public class DeleteRequestFactory extends BaseRequestFactory<DeleteRequest> {

    public DeleteRequestFactory(OCUserService userService, FileService fileService) {
        super(userService, fileService);
    }

    public DeleteRequest createDeleteRequest(String fileToPathToDelete) {
        OCUser initiatingUser = userService.getCurrentlyLoggedInUser().orElse(null);

        FileSystemElement fileToDelete = validatePathAndQueryFSE(fileToPathToDelete);
        OCUser fileToDeleteOwner = fileToDelete.getOwner();

        UploadedFolder parentFolder = validatePathThenQueryAndCastToFolder(fileToDelete.getParentFolderPath());
        OCUser parentFolderOwner = parentFolder.getOwner();

        return new DeleteRequest.DeleteRequestBuilder()
                .fileToDelete(fileToDelete)
                .fileToDeleteOwner(fileToDeleteOwner)
                .parentFolder(parentFolder)
                .parentFolderOwner(parentFolderOwner)
                .initiatingUser(initiatingUser)
                .build();
    }
}
