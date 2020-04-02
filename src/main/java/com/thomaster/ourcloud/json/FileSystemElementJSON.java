package com.thomaster.ourcloud.json;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;

public class FileSystemElementJSON {

    private Long id;
    private String relativePath;
    private String originalName;
    private long fileSize;
    private Long ownerId;
    private String parentFolderPath;

    public FileSystemElementJSON(FileSystemElement source) {
        this.id = source.getId();
        this.relativePath = source.getRelativePath();
        this.originalName = source.getOriginalName();
        this.fileSize = source.getFileSize();
        this.ownerId = source.getOwner().getId();
        this.parentFolderPath = source.getParentFolderPath();
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

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
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

    public static FileSystemElementJSON of(FileSystemElement source) {
        if (source instanceof UploadedFile)
            return new UploadedFileJSON((UploadedFile)source);
        else
            return new UploadedFolderJSON((UploadedFolder) source);
    }
}
