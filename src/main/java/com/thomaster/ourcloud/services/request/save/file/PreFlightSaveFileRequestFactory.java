package com.thomaster.ourcloud.services.request.save.file;

import com.google.common.io.Files;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.base.BaseRequestFactory;
import org.springframework.stereotype.Component;

@Component
public class PreFlightSaveFileRequestFactory extends BaseRequestFactory<PreFlightSaveFileRequest> {

    public PreFlightSaveFileRequestFactory(OCUserService userService, FileService fileService) {
        super(userService, fileService);
    }

    public PreFlightSaveFileRequest createPreFlightSaveFileRequest(String parentFolderPath,
                                                                   String hash,
                                                                   String originalName,
                                                                   long size,
                                                                   boolean shouldOverrideExistingFile) {
        OCUser initiatingUser = userService.getCurrentlyLoggedInUser().orElse(null);
        UploadedFolder parentFolder = validatePathThenQueryAndCastToFolder(parentFolderPath);
        OCUser parentFolderOwner = parentFolder.getOwner();

        return new PreFlightSaveFileRequest.PreFlightSaveFileRequestBuilder()
                .hash(hash)
                .size(size)
                .fileExtension(Files.getFileExtension(originalName))
                .originalName(originalName)
                .shouldOverrideExistingFile(shouldOverrideExistingFile)
                .parentFolder(parentFolder)
                .parentFolderOwner(parentFolderOwner)
                .initiatingUser(initiatingUser)
                .build();
    }
}
