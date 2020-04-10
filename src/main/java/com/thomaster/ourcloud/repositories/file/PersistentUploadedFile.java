package com.thomaster.ourcloud.repositories.file;

import javax.persistence.*;

@Entity
class PersistentUploadedFile extends PersistentFileSystemElement {

    private String filenameOnDisk;

    public String getFilenameOnDisk() {
        return filenameOnDisk;
    }

    public void setFilenameOnDisk(String filenameOnDisk) {
        this.filenameOnDisk = filenameOnDisk;
    }

    @Override
    public String toString() {
        return "UploadedFile{" +
                "originalName='" + getOriginalName() + '\'' +
                '}';
    }
}
