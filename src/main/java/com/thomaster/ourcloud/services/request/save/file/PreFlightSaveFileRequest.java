package com.thomaster.ourcloud.services.request.save.file;

import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.base.BaseSaveRequest;

public class PreFlightSaveFileRequest extends BaseSaveRequest {

    private String hash;
    private long size;
    private String fileExtension;

    private PreFlightSaveFileRequest(OCUser initiatingUser,
                                     OCUser parentFolderOwner,
                                     UploadedFolder parentFolder,
                                     String hash,
                                     long size,
                                     String fileExtension,
                                     boolean shouldOverrideExistingFile,
                                     String originalName) {
        super(initiatingUser, parentFolderOwner, parentFolder, shouldOverrideExistingFile, originalName);
        this.hash = hash;
        this.size = size;
        this.fileExtension = fileExtension;
    }

    public String getHash() {
        return hash;
    }

    public long getSize() {
        return size;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public static class PreFlightSaveFileRequestBuilder extends BaseSaveRequestBuilder<PreFlightSaveFileRequest> {

        private String hash;
        private long size;
        private String fileExtension;

        public PreFlightSaveFileRequestBuilder() {
        }

        public PreFlightSaveFileRequestBuilder hash(String hash){
            this.hash = hash;
            return PreFlightSaveFileRequestBuilder.this;
        }

        public PreFlightSaveFileRequestBuilder size(long size){
            this.size = size;
            return PreFlightSaveFileRequestBuilder.this;
        }

        public PreFlightSaveFileRequestBuilder fileExtension(String fileExtension){
            this.fileExtension = fileExtension;
            return PreFlightSaveFileRequestBuilder.this;
        }

        @Override
        public PreFlightSaveFileRequest build() {

            return new PreFlightSaveFileRequest(initiatingUser,
                    parentFolderOwner,
                    parentFolder,
                    hash,
                    size,
                    fileExtension,
                    shouldOverrideExistingFile,
                    originalName);
        }
    }
}
