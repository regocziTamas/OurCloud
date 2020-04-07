package com.thomaster.ourcloud.services.request.base;

import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import com.thomaster.ourcloud.services.request.RequestValidator;

public class BaseWriteRequestValidator<T extends BaseWriteRequest> extends RequestValidator<T > {

    public BaseWriteRequestValidator() {
        validationElements.add(this::callerHasWriteAccessToParentFolder);
    }

    private void callerHasWriteAccessToParentFolder(BaseWriteRequest writeRequest) {
        OCUser initiatingUser = writeRequest.getInitiatingUser()
                .orElseThrow(RequestValidationException::notLoggedIn);

        if (!writeRequest.getParentFolderOwner().equals(initiatingUser))
            throw RequestValidationException.noWritePermission();
    }
}
