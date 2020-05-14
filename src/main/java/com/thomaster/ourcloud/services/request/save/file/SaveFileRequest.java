package com.thomaster.ourcloud.services.request.save.file;

import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.base.BaseRequest;
import org.springframework.web.multipart.MultipartFile;

public class SaveFileRequest extends BaseRequest {

    private MultipartFile fileToUpload;
    private String uploadToken;
    private String md5Hash;
    private String mimeType;
    private PreFlightSaveFileRequest preFlightSaveFileRequest;

    private SaveFileRequest(OCUser initiatingUser, MultipartFile fileToUpload, String uploadToken, String md5Hash, String mimeType, PreFlightSaveFileRequest preFlightSaveFileRequest) {
        super(initiatingUser);
        this.fileToUpload = fileToUpload;
        this.uploadToken = uploadToken;
        this.md5Hash = md5Hash;
        this.mimeType = mimeType;
        this.preFlightSaveFileRequest = preFlightSaveFileRequest;
    }

    public MultipartFile getFileToUpload() {
        return fileToUpload;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public PreFlightSaveFileRequest getPreFlightSaveFileRequest() {
        return preFlightSaveFileRequest;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static class SaveFileRequestBuilder extends BaseRequest.BaseRequestBuilder<SaveFileRequest> {

        private MultipartFile fileToUpload;
        private String uploadToken;
        private String md5Hash;
        private PreFlightSaveFileRequest preFlightSaveFileRequest;
        private String mimeType;

        public SaveFileRequest.SaveFileRequestBuilder preFlightSaveFileRequest(PreFlightSaveFileRequest preFlightSaveFileRequest){
            this.preFlightSaveFileRequest = preFlightSaveFileRequest;
            return SaveFileRequest.SaveFileRequestBuilder.this;
        }

        public SaveFileRequest.SaveFileRequestBuilder md5Hash(String md5Hash){
            this.md5Hash = md5Hash;
            return SaveFileRequest.SaveFileRequestBuilder.this;
        }

        public SaveFileRequest.SaveFileRequestBuilder mimeType(String mimeType){
            this.mimeType = mimeType;
            return SaveFileRequest.SaveFileRequestBuilder.this;
        }

        public SaveFileRequest.SaveFileRequestBuilder uploadToken(String uploadToken){
            this.uploadToken = uploadToken;
            return SaveFileRequest.SaveFileRequestBuilder.this;
        }

        public SaveFileRequest.SaveFileRequestBuilder fileToUpload(MultipartFile fileToUpload){
            this.fileToUpload = fileToUpload;
            return SaveFileRequest.SaveFileRequestBuilder.this;
        }

        @Override
        public SaveFileRequest build() {

            return new SaveFileRequest(initiatingUser,
                    fileToUpload,
                    uploadToken,
                    md5Hash,
                    mimeType,
                    preFlightSaveFileRequest);
        }
    }
}
