package com.thomaster.ourcloud.services.request.save.file;

import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.base.BaseSaveRequest;
import org.springframework.web.multipart.MultipartFile;

public class SaveFileRequest extends BaseSaveRequest {

    private MultipartFile file;
    private long size;
    private String fileExtension;
    private String mimeType;

    private SaveFileRequest(OCUser initiatingUser,
                            OCUser parentFolderOwner,
                            UploadedFolder parentFolder,
                            MultipartFile file,
                            long size,
                            String fileExtension,
                            boolean shouldOverrideExistingFile,
                            String originalName,
                            String mimeType) {
        super(initiatingUser, parentFolderOwner, parentFolder, shouldOverrideExistingFile, originalName);
        this.file = file;
        this.size = size;
        this.fileExtension = fileExtension;
        this.mimeType = mimeType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public long getSize() {
        return size;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static class SaveFileRequestBuilder extends BaseSaveRequestBuilder<SaveFileRequest> {

        private MultipartFile file;
        private long size;
        private String fileExtension;
        private String mimeType;

        public SaveFileRequestBuilder() {
        }

        public SaveFileRequestBuilder file(MultipartFile file){
            this.file = file;
            return SaveFileRequestBuilder.this;
        }

        public SaveFileRequestBuilder size(long size){
            this.size = size;
            return SaveFileRequestBuilder.this;
        }

        public SaveFileRequestBuilder fileExtension(String fileExtension){
            this.fileExtension = fileExtension;
            return SaveFileRequestBuilder.this;
        }

        public SaveFileRequestBuilder mimeType(String mimeType){
            this.mimeType = mimeType;
            return SaveFileRequestBuilder.this;
        }

        @Override
        public SaveFileRequest build() {

            return new SaveFileRequest(initiatingUser,
                    parentFolderOwner,
                    parentFolder,
                    file,
                    size,
                    fileExtension,
                    shouldOverrideExistingFile,
                    originalName,
                    mimeType);
        }
    }
}
