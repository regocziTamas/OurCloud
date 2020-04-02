package com.thomaster.ourcloud.json;

import com.thomaster.ourcloud.model.filesystem.UploadedFolder;

import java.util.Set;
import java.util.stream.Collectors;

public class UploadedFolderJSON extends FileSystemElementJSON {

    private Set<ContainedFSEInfoJSON> containedFiles;

    UploadedFolderJSON(UploadedFolder source) {
        super(source);
        this.containedFiles = extractContainedFiles(source);
    }

    public Set<ContainedFSEInfoJSON> getContainedFiles() {
        return containedFiles;
    }

    public void setContainedFiles(Set<ContainedFSEInfoJSON> containedFiles) {
        this.containedFiles = containedFiles;
    }

    private Set<ContainedFSEInfoJSON> extractContainedFiles(UploadedFolder source) {
        return source.getContainedFileInfos()
                .stream()
                .map(ContainedFSEInfoJSON::new)
                .collect(Collectors.toSet());
    }
}
