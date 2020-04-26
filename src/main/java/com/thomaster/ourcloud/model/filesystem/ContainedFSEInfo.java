package com.thomaster.ourcloud.model.filesystem;

import com.thomaster.ourcloud.model.user.OCUser;

public class ContainedFSEInfo {

    private Long id;

    private String relativePath;

    private String originalName;

    private OCUser owner;

    private String parentFolderPath;

    private long size;

    private boolean isFolder;

    private String mimeType;

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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        String className = isFolder ? "Folder" : "File";

        return "Contained " + className + ": " + originalName + " | " + getSize() + " bytes";
    }
}
