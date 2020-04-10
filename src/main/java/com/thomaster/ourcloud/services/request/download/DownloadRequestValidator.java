package com.thomaster.ourcloud.services.request.download;

import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import com.thomaster.ourcloud.services.request.read.ReadRequest;
import com.thomaster.ourcloud.services.request.read.ReadRequestValidator;
import org.springframework.stereotype.Component;

@Component
public class DownloadRequestValidator extends ReadRequestValidator {

    public DownloadRequestValidator() {
        validationElements.add(this::targetIsFile);
    }

    private void targetIsFile(ReadRequest readRequest) {
        if (!(readRequest.getFileToRead() instanceof UploadedFile))
            throw RequestValidationException.notAFile(readRequest.getFileToRead().getRelativePath());
    }

}
