package com.thomaster.ourcloud.services.request.read;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.base.BaseRequest;

public class ReadRequest extends BaseRequest {

    private OCUser fileToReadOwner;
    private FileSystemElement fileToRead;

    private ReadRequest(OCUser initiatingUser,
                        FileSystemElement fileToRead,
                        OCUser fileToReadOwner) {
        super(initiatingUser);
        this.fileToReadOwner = fileToReadOwner;
        this.fileToRead = fileToRead;
    }

    public OCUser getFileToReadOwner() {
        return fileToReadOwner;
    }

    public FileSystemElement getFileToRead() {
        return fileToRead;
    }

    public static class ReadRequestBuilder extends BaseRequestBuilder<ReadRequest> {
        private OCUser fileToReadOwner;
        private FileSystemElement fileToRead;

        public ReadRequestBuilder setFileToRead(FileSystemElement fileToRead) {
            this.fileToRead = fileToRead;
            return this;
        }

        public ReadRequestBuilder setFileToReadOwner(OCUser fileToReadOwner) {
            this.fileToReadOwner = fileToReadOwner;
            return this;
        }

        public ReadRequest build() {
            return new ReadRequest(initiatingUser, fileToRead, fileToReadOwner);
        }
    }
}
