package com.thomaster.ourcloud.repositories.file;

import javax.persistence.*;

@Entity
class PersistentUploadedFile extends PersistentFileSystemElement {

    @Override
    public String toString() {
        return "UploadedFile{" +
                "originalName='" + getOriginalName() + '\'' +
                '}';
    }
}
