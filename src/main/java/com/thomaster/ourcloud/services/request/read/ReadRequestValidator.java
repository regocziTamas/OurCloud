package com.thomaster.ourcloud.services.request.read;

import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import com.thomaster.ourcloud.services.request.RequestValidator;
import org.springframework.stereotype.Component;

@Component
public class ReadRequestValidator extends RequestValidator<ReadRequest> {

    public ReadRequestValidator() {
        validationElements.add(this::callerHasReadAccessToFolder);
    }

    private void callerHasReadAccessToFolder(ReadRequest readRequest) {
        OCUser initiatingUser = readRequest.getInitiatingUser()
                .orElseThrow(RequestValidationException::notLoggedIn);

        if (!readRequest.getFileToReadOwner().equals(initiatingUser))
            throw RequestValidationException.noReadPermission();
    }
}
