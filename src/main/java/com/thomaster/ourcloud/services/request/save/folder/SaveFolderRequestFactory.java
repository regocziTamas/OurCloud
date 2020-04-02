package com.thomaster.ourcloud.services.request.save.folder;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.base.BaseRequestFactory;
import org.springframework.stereotype.Component;

@Component
public class SaveFolderRequestFactory extends BaseRequestFactory<SaveFolderRequest> {

    public SaveFolderRequestFactory(OCUserService userService, FileService fileService) {
        super(userService, fileService);
    }

    public SaveFolderRequest createSaveFolderRequest(String parentFolderPath, String newFolderName, boolean shouldOverrideExistingFile) {
        OCUser initiatingUser = userService.getCurrentlyLoggedInUser().orElse(null);
        UploadedFolder parentFolder = validatePathThenQueryAndCastToFolder(parentFolderPath);
        OCUser parentFolderOwner = parentFolder.getOwner();

        return new SaveFolderRequest.SaveFolderRequestBuilder()
                .originalName(newFolderName)
                .shouldOverrideExistingFile(shouldOverrideExistingFile)
                .parentFolder(parentFolder)
                .parentFolderOwner(parentFolderOwner)
                .initiatingUser(initiatingUser)
                .build();
    }
}
