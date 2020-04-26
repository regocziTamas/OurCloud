package com.thomaster.ourcloud.json;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;

public class UploadedFileJSON extends FileSystemElementJSON {

    private String mimeType;

    UploadedFileJSON(UploadedFile source) {
        super(source);
        this.mimeType = source.getMimeType();
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
