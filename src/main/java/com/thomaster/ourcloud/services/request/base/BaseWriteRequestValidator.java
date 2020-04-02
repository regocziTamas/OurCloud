package com.thomaster.ourcloud.services.request.base;

import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.RequestValidator;

public class BaseWriteRequestValidator<T extends BaseWriteRequest> extends RequestValidator<T > {

    public BaseWriteRequestValidator() {
        validationElements.add(this::callerHasWriteAccessToParentFolder);
    }

    private void callerHasWriteAccessToParentFolder(BaseWriteRequest writeRequest) {
        OCUser initiatingUser = writeRequest.getInitiatingUser()
                .orElseThrow(() -> new IllegalArgumentException("You must be logged in to perform this operation!"));

        if (!writeRequest.getParentFolderOwner().equals(initiatingUser))
            throw new IllegalArgumentException("You have no write access to this file!");
    }
}
