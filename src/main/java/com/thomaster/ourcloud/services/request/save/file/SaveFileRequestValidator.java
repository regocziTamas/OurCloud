package com.thomaster.ourcloud.services.request.save.file;

import com.thomaster.ourcloud.services.request.RequestValidationException;
import com.thomaster.ourcloud.services.request.RequestValidator;
import org.springframework.stereotype.Service;

@Service
public class SaveFileRequestValidator extends RequestValidator<SaveFileRequest> {


    public SaveFileRequestValidator() {
        validationElements.add(this::validatePreFlightRequestExists);
        validationElements.add(this::validateHash);
    }

    private void validatePreFlightRequestExists(SaveFileRequest saveFileRequest) {

        PreFlightSaveFileRequest preFlightSaveFileRequest = saveFileRequest.getPreFlightSaveFileRequest();

        if(preFlightSaveFileRequest == null)
            throw new RequestValidationException("ReqValExp10", "Invalid upload token!");
    }

    private void validateHash(SaveFileRequest saveFileRequest) {

        String hashFromPreFlightRequest = saveFileRequest.getPreFlightSaveFileRequest().getHash();
        String actualHashOfFile = saveFileRequest.getMd5Hash();

        if(!hashFromPreFlightRequest.equals(actualHashOfFile))
            throw new RequestValidationException("ReqValExc09", "Hash from preflight request: " + hashFromPreFlightRequest + ", actual hash of file: " + actualHashOfFile);
    }
}
