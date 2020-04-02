package com.thomaster.ourcloud.services.request.base;

import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;

public abstract class BaseWriteRequest extends BaseRequest {

    private OCUser parentFolderOwner;
    private UploadedFolder parentFolder;

    protected BaseWriteRequest(OCUser initiatingUser, OCUser parentFolderOwner, UploadedFolder parentFolder) {
        super(initiatingUser);
        this.parentFolderOwner = parentFolderOwner;
        this.parentFolder = parentFolder;
    }

    public OCUser getParentFolderOwner() {
        return parentFolderOwner;
    }

    public UploadedFolder getParentFolder() {
        return parentFolder;
    }

    public static abstract class BaseWriteRequestBuilder<T extends BaseWriteRequest> extends BaseRequestBuilder<T>{

        protected OCUser parentFolderOwner;
        protected UploadedFolder parentFolder;

        public BaseWriteRequestBuilder() {
        }

        public BaseWriteRequestBuilder<T> parentFolderOwner(OCUser parentFolderOwner){
            this.parentFolderOwner = parentFolderOwner;
            return BaseWriteRequestBuilder.this;
        }

        public BaseWriteRequestBuilder<T> parentFolder(UploadedFolder parentFolder){
            this.parentFolder = parentFolder;
            return BaseWriteRequestBuilder.this;
        }

        public abstract T build();
    }
}
