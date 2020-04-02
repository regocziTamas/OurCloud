package com.thomaster.ourcloud.services.request.delete;

import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.request.base.BaseWriteRequestValidator;
import org.springframework.stereotype.Component;

@Component
public class DeleteRequestValidator extends BaseWriteRequestValidator<DeleteRequest> {

    public DeleteRequestValidator() {
        validationElements.add(this::callerHasWriteAccessToFile);
    }

    private void callerHasWriteAccessToFile(DeleteRequest deleteRequest) {
        OCUser initiatingUser = deleteRequest.getInitiatingUser()
                .orElseThrow(() -> new IllegalArgumentException("You must be logged in to perform this operation!"));

        if (!deleteRequest.getFileToDeleteOwner().equals(initiatingUser))
            throw new IllegalArgumentException("You have no write access to this file!");
    }
}
