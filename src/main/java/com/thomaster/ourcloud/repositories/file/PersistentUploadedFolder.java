package com.thomaster.ourcloud.repositories.file;

import javax.persistence.*;

@Entity
class PersistentUploadedFolder extends PersistentFileSystemElement {

    @Override
    public String toString() {
        return "UploadedFolder{" +
                "originalName='" + getOriginalName() + '\'' +
                '}';
    }
}
