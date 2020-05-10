package com.thomaster.ourcloud.services.request.save.file;

import com.thomaster.ourcloud.services.request.RequestValidationException;
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
            throw RequestValidationException.forbiddenExtension(saveFileRequest.getFileExtension());
    }

    private void callerHasEnoughStorageLeft(SaveFileRequest saveFolderRequest) {
        if(saveFolderRequest.getSize() + saveFolderRequest.getParentFolderOwner().getUsedBytes() > 500000000L)
            throw RequestValidationException.storageLimitExceeded();
    }

    private void fileNameIsUniqueInFolder(SaveFileRequest saveFileRequest) {
        boolean nameCollidesWithExistingFile = saveFileRequest.getParentFolder().getContainedFileInfos()
                .stream()
                .filter(fse ->  !fse.isFolder())
                .anyMatch(file -> file.getOriginalName().equals(saveFileRequest.getOriginalName()));

        if(nameCollidesWithExistingFile && !saveFileRequest.isShouldOverrideExistingFile())
            throw RequestValidationException.fileNameNotUnique(saveFileRequest.getOriginalName(), saveFileRequest.getParentFolder().getOriginalName());
    }
}
