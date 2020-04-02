package com.thomaster.ourcloud.services;

import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import org.springframework.stereotype.Service;

@Service
public class FilePathService {

    private OCUserService userService;
    private String absolutePathPart = System.getenv("ABSOLUTE_USER_PATH_PART");

    public FilePathService(OCUserService userService) {
        this.userService = userService;
    }

//    public String createAbsolutePathForFile(String filename, String relativeUserPath) {
//
//        if(filename == null || filename.equals(""))
//            throw new RuntimeException("Filename cannot be empty or null");
//
//        String fullPath = absolutePathPart +
//                "/" +
//                userService.getCurrentlyLoggedInUsername() +
//                "/" +
//                formatRelativeUserPath(relativeUserPath) +
//                filename;
//
//        System.out.println(fullPath);
//
//        return fullPath;
//    }

    public String appendAbsolutePathPart(String relativePathPart) {
        return absolutePathPart + relativePathPart;
    }

    private String formatRelativeUserPath(String relativeUserPath) {
        return relativeUserPath.equals("") ? relativeUserPath : relativeUserPath + ".";
    }

    public String createRelativePathToFile(FileSystemElement parentFolder, String filename) {
        return parentFolder.getRelativePath() + "." + filename;
    }

}
