package com.thomaster.ourcloud.services.request.read;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.base.BaseRequestFactory;
import com.thomaster.ourcloud.services.request.delete.DeleteRequest;
import org.springframework.stereotype.Service;

@Service
public class ReadRequestFactory extends BaseRequestFactory<DeleteRequest> {

    public ReadRequestFactory(OCUserService userService, FileService fileService) {
        super(userService, fileService);
    }

    public ReadRequest createReadRequest(FileSystemElement fileSystemElement) {

        OCUser initiatingUser = userService.getCurrentlyLoggedInUser().orElse(null);

        return new ReadRequest.ReadRequestBuilder()
                .setFileToRead(fileSystemElement)
                .setFileToReadOwner(fileSystemElement.getOwner())
                .initiatingUser(initiatingUser)
                .build();
    }
}
