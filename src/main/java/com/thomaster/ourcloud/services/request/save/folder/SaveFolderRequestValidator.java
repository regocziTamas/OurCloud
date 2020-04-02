package com.thomaster.ourcloud.services.request.save.folder;

import com.thomaster.ourcloud.model.filesystem.ContainedFSEInfo;
import com.thomaster.ourcloud.services.request.base.BaseWriteRequestValidator;
import org.springframework.stereotype.Service;

@Service
public class SaveFolderRequestValidator extends BaseWriteRequestValidator<SaveFolderRequest> {

    public SaveFolderRequestValidator() {
        validationElements.add(this::newFolderNameNotExistsInParentFolder);
    }

    private void newFolderNameNotExistsInParentFolder(SaveFolderRequest saveFolderRequest) {
        boolean newNameCollidesWithExistingFolder = saveFolderRequest.getParentFolder().getContainedFileInfos()
                .stream()
                .filter(ContainedFSEInfo::isFolder)
                .anyMatch(folder -> folder.getOriginalName().equals(saveFolderRequest.getOriginalName()));

        if(newNameCollidesWithExistingFolder)
            throw new IllegalArgumentException("Parent folder already contains a folder with the provided name");
    }
}
