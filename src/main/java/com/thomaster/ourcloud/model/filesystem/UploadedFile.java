package com.thomaster.ourcloud.model.filesystem;

public class UploadedFile extends FileSystemElement {

    private String filenameOnDisk;

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
}
