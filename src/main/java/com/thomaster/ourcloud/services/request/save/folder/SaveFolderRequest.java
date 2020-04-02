package com.thomaster.ourcloud.services.request.save.folder;

import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.base.BaseSaveRequest;

public class SaveFolderRequest extends BaseSaveRequest {

    private SaveFolderRequest(OCUser initiatingUser, OCUser parentFolderOwner, UploadedFolder parentFolder, String newFolderName, boolean shouldOverrideExistingFile) {
        super(initiatingUser, parentFolderOwner, parentFolder, shouldOverrideExistingFile, newFolderName);
    }

    public static class SaveFolderRequestBuilder extends BaseSaveRequestBuilder<SaveFolderRequest>{

        public SaveFolderRequest build() {
            return new SaveFolderRequest(initiatingUser, parentFolderOwner, parentFolder, originalName, shouldOverrideExistingFile);
        }
    }
}
