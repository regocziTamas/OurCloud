package com.thomaster.ourcloud.repositories.file;

import javax.persistence.*;

@Entity
class PersistentUploadedFile extends PersistentFileSystemElement {

    private String filenameOnDisk;

    private String mimeType;

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

    @Override
    public String toString() {
        return "UploadedFile{" +
                "originalName='" + getOriginalName() + '\'' +
                '}';
    }
}
