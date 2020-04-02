package com.thomaster.ourcloud.services.request.read;

import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.RequestValidator;
import org.springframework.stereotype.Component;

@Component
public class ReadRequestValidator extends RequestValidator<ReadRequest> {

    public ReadRequestValidator() {
        validationElements.add(this::callerHasReadAccessToFolder);
    }

    private void callerHasReadAccessToFolder(ReadRequest readRequest) {
        OCUser initiatingUser = readRequest.getInitiatingUser()
                .orElseThrow(() -> new IllegalArgumentException("You must be logged in to perform this operation!"));

        if (!readRequest.getFileToReadOwner().equals(initiatingUser))
            throw new IllegalArgumentException("You have no access to this file!");
    }
}
