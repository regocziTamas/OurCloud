package com.thomaster.ourcloud.model.filesystem;

public class UploadedFile extends FileSystemElement {

    private String filenameOnDisk;

    private String mimeType;

    @Override
    String getToStringClassName() {
        return "File";
    }

    public String getFilenameOnDisk() {
        return filenameOnDisk;
    }

    public void setFilenameOnDisk(String filenameOnDisk) {
        this.filenameOnDisk = filenameOnDisk;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
