package com.thomaster.ourcloud.listeners;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.repositories.UserRepository;
import com.thomaster.ourcloud.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class SuccessfullLoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileService fileService;

//    @Autowired
//    FileCache fileCache;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        OCUser currentlyLoggedInUser = userRepository.findByUsername(event.getAuthentication().getName());

        FileSystemElement rootFolder = fileService.findFSElementWithContainedFilesByPath_noChecks(currentlyLoggedInUser.getUsername());

        //System.out.println( PersistentFileStructureFormatter.formatRecursive(rootFolder));

        //UserFileCache userFileCache = fileCache.addCacheForUserIfNotExists(currentlyLoggedInUser);

       // userFileCache.addWithFlattening(rootFolder);

        System.out.println(currentlyLoggedInUser.getUsername() +" has logged in, file cache should be initialized now!");
    }
}
