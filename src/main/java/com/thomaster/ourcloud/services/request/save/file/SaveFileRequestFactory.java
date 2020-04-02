package com.thomaster.ourcloud.services.request.save.file;

import com.google.common.io.Files;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.base.BaseRequestFactory;
import com.thomaster.ourcloud.services.request.delete.DeleteRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class SaveFileRequestFactory extends BaseRequestFactory<SaveFileRequest> {

    public SaveFileRequestFactory(OCUserService userService, FileService fileService) {
        super(userService, fileService);
    }

    public SaveFileRequest createSaveFileRequest(String parentFolderPath, MultipartFile file, boolean shouldOverrideExistingFile) {
        OCUser initiatingUser = userService.getCurrentlyLoggedInUser().orElse(null);
        UploadedFolder parentFolder = validatePathThenQueryAndCastToFolder(parentFolderPath);
        OCUser parentFolderOwner = parentFolder.getOwner();

        return new SaveFileRequest.SaveFileRequestBuilder()
                .file(file)
                .size(file.getSize())
                .fileExtension(Files.getFileExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .shouldOverrideExistingFile(shouldOverrideExistingFile)
                .parentFolder(parentFolder)
                .parentFolderOwner(parentFolderOwner)
                .initiatingUser(initiatingUser)
                .build();
    }
}
