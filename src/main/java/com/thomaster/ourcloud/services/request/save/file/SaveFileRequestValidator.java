package com.thomaster.ourcloud.services.request.save.file;

import com.thomaster.ourcloud.services.request.base.BaseWriteRequestValidator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SaveFileRequestValidator extends BaseWriteRequestValidator<SaveFileRequest> {

    public SaveFileRequestValidator() {
        validationElements.add(this::fileExtensionIsPermitted);
        validationElements.add(this::callerHasEnoughStorageLeft);
        validationElements.add(this::fileNameIsUniqueInFolder);
    }

    private void fileExtensionIsPermitted(SaveFileRequest saveFileRequest) {
        List<String> forbiddenExtensions = Arrays.asList("exe", "sh");

        if(forbiddenExtensions.contains(saveFileRequest.getFileExtension()))
            throw new IllegalArgumentException("The file you are trying to upload has a forbidden extension: " + saveFileRequest.getFileExtension());
    }

    private void callerHasEnoughStorageLeft(SaveFileRequest saveFolderRequest) {
        if(saveFolderRequest.getSize() + saveFolderRequest.getParentFolderOwner().getUsedBytes() > 500000000)
            throw new IllegalArgumentException("You have exceeded your storage limit!");
    }

    private void fileNameIsUniqueInFolder(SaveFileRequest saveFileRequest) {
        boolean nameCollidesWithExistingFile = saveFileRequest.getParentFolder().getContainedFileInfos()
                .stream()
                .filter(fse ->  !fse.isFolder())
                .anyMatch(file -> file.getOriginalName().equals(saveFileRequest.getOriginalName()));

        if(nameCollidesWithExistingFile && !saveFileRequest.isShouldOverrideExistingFile())
            throw new IllegalArgumentException("File with name \""
                    + saveFileRequest.getOriginalName()
                    + "\" already exists in Folder \""
                    + saveFileRequest.getParentFolder().getRelativePath() + "\"");
    }
}
