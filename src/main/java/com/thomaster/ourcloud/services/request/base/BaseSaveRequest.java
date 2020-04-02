package com.thomaster.ourcloud.services.request.base;

import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;

public abstract class BaseSaveRequest extends BaseWriteRequest {

    private boolean shouldOverrideExistingFile;
    private String originalName;

    protected BaseSaveRequest(OCUser initiatingUser, OCUser parentFolderOwner, UploadedFolder parentFolder, boolean shouldOverrideExistingFile, String originalName) {
        super(initiatingUser, parentFolderOwner, parentFolder);
        this.shouldOverrideExistingFile = shouldOverrideExistingFile;
        this.originalName = originalName;
    }

    public boolean isShouldOverrideExistingFile() {
        return shouldOverrideExistingFile;
    }

    public String getOriginalName() {
        return originalName;
    }

    public static abstract class BaseSaveRequestBuilder<T extends BaseSaveRequest> extends BaseWriteRequestBuilder<T> {

        protected boolean shouldOverrideExistingFile;
        protected String originalName;

        public BaseSaveRequestBuilder() {
        }

        public BaseSaveRequestBuilder<T> shouldOverrideExistingFile(boolean shouldOverrideExistingFile){
            this.shouldOverrideExistingFile = shouldOverrideExistingFile;
            return BaseSaveRequestBuilder.this;
        }

        public BaseSaveRequestBuilder<T> originalName(String originalName){
            this.originalName = originalName;
            return BaseSaveRequestBuilder.this;
        }

        public abstract T build();
    }
}
