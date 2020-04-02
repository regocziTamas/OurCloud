package com.thomaster.ourcloud.repositories.file;

import com.thomaster.ourcloud.model.filesystem.ContainedFSEInfo;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;

import java.util.Set;
import java.util.stream.Collectors;

public class PersistentFSEHierarchyConverter {

    private Set<PersistentFileSystemElement> persistedResult;
    private String rootElementPath;

    public PersistentFSEHierarchyConverter(Set<PersistentFileSystemElement> persistedResult, String rootElementPath) {
        this.persistedResult = persistedResult;
        this.rootElementPath = rootElementPath;
    }

    public FileSystemElement convertAndBuildHierarchy() {

        PersistentFileSystemElement rootElement = persistedResult
                .stream()
                .filter(element -> element.getRelativePath().equals(rootElementPath))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("The supplied PersistedFileSystemElements did not contain the supplied root element path"));

        FileSystemElement convertedRootElement = PersistentToDomainConverter.convertToDomain(rootElement);

        if(persistedResult.size() == 1)
            return convertedRootElement;

        UploadedFolder convertedFolder = (UploadedFolder) convertedRootElement;

        Set<ContainedFSEInfo> containedFSEInfos = persistedResult
                .stream()
                .filter(element -> !element.getRelativePath().equals(rootElementPath))
                .map(PersistentToDomainConverter::convertToInfo)
                .collect(Collectors.toSet());

        convertedFolder.setContainedFileInfos(containedFSEInfos);

        return convertedFolder;
    };
}
