package com.thomaster.ourcloud.repositories.file;

import com.thomaster.ourcloud.model.user.OCUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
abstract class PersistentFileSystemElement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Type(type = "com.thomaster.ourcloud.repositories.file.LTreeType")
    @Column(columnDefinition="ltree")
    private String relativePath;

    private String originalName;

    @ManyToOne(fetch = FetchType.EAGER)
    private OCUser owner;

    private String parentFolderPath;

    private long fileSize;

    PersistentFileSystemElement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public OCUser getOwner() {
        return owner;
    }

    public void setOwner(OCUser owner) {
        this.owner = owner;
    }

    public String getParentFolderPath() {
        return parentFolderPath;
    }

    public void setParentFolderPath(String parentFolderPath) {
        this.parentFolderPath = parentFolderPath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "FileSystemElement{" +
                "originalName='" + originalName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersistentFileSystemElement that = (PersistentFileSystemElement) o;
        return relativePath.equals(that.relativePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativePath);
    }


}
