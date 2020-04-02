package com.thomaster.ourcloud.json;

import com.thomaster.ourcloud.model.filesystem.ContainedFSEInfo;

public class ContainedFSEInfoJSON {

    private Long id;

    private String relativePath;

    private String originalName;

    private Long ownerId;

    private String parentFolderPath;

    private long size;

    private boolean isFolder;

    public ContainedFSEInfoJSON(ContainedFSEInfo source) {
        this.id = source.getId();
        this.relativePath = source.getRelativePath();
        this.originalName = source.getOriginalName();
        this.ownerId = source.getOwner().getId();
        this.parentFolderPath = source.getParentFolderPath();
        this.size = source.getSize();
        this.isFolder = source.isFolder();
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
}
