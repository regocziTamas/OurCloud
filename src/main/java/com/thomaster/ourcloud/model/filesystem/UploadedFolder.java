package com.thomaster.ourcloud.model.filesystem;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UploadedFolder extends FileSystemElement {

    private Set<ContainedFSEInfo> containedFileInfos = new HashSet<>();

    public Set<ContainedFSEInfo> getContainedFileInfos() {
        return containedFileInfos;
    }

    public void setContainedFileInfos(Set<ContainedFSEInfo> containedFileInfos) {
        this.containedFileInfos = containedFileInfos;
    }

    @Override
    String getToStringClassName() {
        return "Folder";
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n" +
                concatContainedFileInfos();
    }

    private String concatContainedFileInfos() {
        return containedFileInfos
                .stream()
                .map(info -> "    - " + info.toString())
                .collect(Collectors.joining("\n"));
    }
}
