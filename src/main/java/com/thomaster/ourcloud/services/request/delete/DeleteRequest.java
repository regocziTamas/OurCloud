package com.thomaster.ourcloud.services.request.delete;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.base.BaseRequest;
import com.thomaster.ourcloud.services.request.base.BaseWriteRequest;

public class DeleteRequest extends BaseWriteRequest {

    private OCUser fileToDeleteOwner;
    private FileSystemElement fileToDelete;

    private DeleteRequest(OCUser initiatingUser, OCUser fileToDeleteOwner, FileSystemElement fileToDelete, OCUser parentFolderOwner, UploadedFolder parentFolder) {
        super(initiatingUser, parentFolderOwner, parentFolder);
        this.fileToDeleteOwner = fileToDeleteOwner;
        this.fileToDelete = fileToDelete;
    }

    public OCUser getFileToDeleteOwner() {
        return fileToDeleteOwner;
    }

    public FileSystemElement getFileToDelete() {
        return fileToDelete;
    }

    public static class DeleteRequestBuilder extends BaseWriteRequestBuilder<DeleteRequest>{

        private OCUser fileToDeleteOwner;
        private FileSystemElement fileToDelete;

        public DeleteRequestBuilder() {
        }

        public DeleteRequestBuilder fileToDeleteOwner(OCUser fileToDeleteOwner) {
            this.fileToDeleteOwner = fileToDeleteOwner;
            return DeleteRequestBuilder.this;
        }

        public DeleteRequestBuilder fileToDelete(FileSystemElement fileToDelete) {
            this.fileToDelete = fileToDelete;
            return DeleteRequestBuilder.this;
        }

        public DeleteRequest build() {

            return new DeleteRequest(initiatingUser, fileToDeleteOwner, fileToDelete, parentFolderOwner, parentFolder);
        }
    }
}
