package com.thomaster.ourcloud.model.filesystem;

import com.thomaster.ourcloud.model.user.OCUser;

public abstract class FileSystemElement {

    private Long id;

    private String relativePath;

    private String originalName;

    private OCUser owner;

    private String parentFolderPath;

    private long fileSize;


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

    abstract String getToStringClassName();

    @Override
    public String toString() {
        return "[" + getToStringClassName() + "] " +
                getOriginalName() + " | " +
                getFileSize() + " bytes";
    }
}
