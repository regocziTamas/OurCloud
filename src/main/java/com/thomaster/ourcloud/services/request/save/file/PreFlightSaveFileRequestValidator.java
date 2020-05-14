package com.thomaster.ourcloud.services.request.save.file;

import com.thomaster.ourcloud.services.request.RequestValidationException;
import com.thomaster.ourcloud.services.request.base.BaseWriteRequestValidator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PreFlightSaveFileRequestValidator extends BaseWriteRequestValidator<PreFlightSaveFileRequest> {

    public PreFlightSaveFileRequestValidator() {
        validationElements.add(this::fileExtensionIsPermitted);
        validationElements.add(this::callerHasEnoughStorageLeft);
        validationElements.add(this::fileNameIsUniqueInFolder);
    }

    private void fileExtensionIsPermitted(PreFlightSaveFileRequest preFlightSaveFileRequest) {
        List<String> forbiddenExtensions = Arrays.asList("exe", "sh");

        if(forbiddenExtensions.contains(preFlightSaveFileRequest.getFileExtension()))
            throw RequestValidationException.forbiddenExtension(preFlightSaveFileRequest.getFileExtension());
    }

    private void callerHasEnoughStorageLeft(PreFlightSaveFileRequest saveFolderRequest) {
        if(saveFolderRequest.getSize() + saveFolderRequest.getParentFolderOwner().getUsedBytes() > 500000000L)
            throw RequestValidationException.storageLimitExceeded();
    }

    private void fileNameIsUniqueInFolder(PreFlightSaveFileRequest preFlightSaveFileRequest) {
        boolean nameCollidesWithExistingFile = preFlightSaveFileRequest.getParentFolder().getContainedFileInfos()
                .stream()
                .filter(fse ->  !fse.isFolder())
                .anyMatch(file -> file.getOriginalName().equals(preFlightSaveFileRequest.getOriginalName()));

        if(nameCollidesWithExistingFile && !preFlightSaveFileRequest.isShouldOverrideExistingFile())
            throw RequestValidationException.fileNameNotUnique(preFlightSaveFileRequest.getOriginalName(), preFlightSaveFileRequest.getParentFolder().getOriginalName());
    }
}
