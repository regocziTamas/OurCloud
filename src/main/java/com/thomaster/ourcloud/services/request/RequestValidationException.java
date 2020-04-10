package com.thomaster.ourcloud.services.request;

import java.text.MessageFormat;

public class RequestValidationException extends RuntimeException {

    public static final String NAME_NOT_UNIQUE_CODE = "ReqValExc01";
    public static final String NAME_NOT_UNIQUE_MSG = "Folder with name ''{0}'' already exists in folder ''{1}''";

    public static final String FORBIDDEN_EXT_CODE = "ReqValExc02";
    public static final String FORBIDDEN_EXT_MSG = "The file you are trying to upload has a forbidden extension: {0}";

    public static final String NO_MORE_STORAGE_CODE = "ReqValExc03";
    public static final String NO_MORE_STORAGE_MSG = "You have exceeded your storage limit!";

    public static final String FILENAME_NOT_UNIQUE_CODE = "ReqValExc04";
    public static final String FILENAME_NOT_UNIQUE_MSG = "File with name ''{0}'' already exists in folder ''{1}''";

    public static final String NOT_LOGGED_IN_CODE = "ReqValExc05";
    public static final String NOT_LOGGED_IN_MSG = "You must be logged in to perform this operation!";

    public static final String NO_READ_ACCESS_CODE = "ReqValExc06";
    public static final String NO_READ_ACCESS_MSG = "You have no access to this file!";

    public static final String NO_WRITE_PERM_CODE = "ReqValExc07";
    public static final String NO_WRITE_PERM_MSG = "You have no write access to this file!";

    public static final String NO_FSE_FOUND_CODE = "ReqValExc08";
    public static final String NO_FSE_FOUND_MSG = "No {0} exists with the following path: {1}";

    public static final String NOT_A_FILE_DOWNLOAD_CODE = "ReqValExc09";
    public static final String NOT_A_FILE_DOWNLOAD_MSG = "''{0}'' is not a file, it cannot be downloaded!";


    public String errorCode;
    public String errorMsg;

    public RequestValidationException(String errorCode, String errorMsg) {
        super(MessageFormat.format("[{0}] {1}", errorCode, errorMsg));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public static RequestValidationException folderNameNotUnique(String folderName, String parentFolderPath) {
        String msg = MessageFormat.format(NAME_NOT_UNIQUE_MSG, folderName, parentFolderPath);
        return new RequestValidationException(NAME_NOT_UNIQUE_CODE, msg);
    }

    public static RequestValidationException fileNameNotUnique(String fileName, String parentFolderPath) {
        String msg = MessageFormat.format(FILENAME_NOT_UNIQUE_MSG, fileName, parentFolderPath);
        return new RequestValidationException(FILENAME_NOT_UNIQUE_CODE, msg);
    }

    public static RequestValidationException forbiddenExtension(String extension) {
        String msg = MessageFormat.format(FORBIDDEN_EXT_MSG, extension);
        return new RequestValidationException(FORBIDDEN_EXT_CODE, msg);
    }

    public static RequestValidationException noFileSystemElementFound(String path, String type) {
        String msg = MessageFormat.format(NO_FSE_FOUND_MSG, type, path);
        return new RequestValidationException(NO_FSE_FOUND_CODE, msg);
    }

    public static RequestValidationException storageLimitExceeded() {
        return new RequestValidationException(NO_MORE_STORAGE_CODE, NO_MORE_STORAGE_MSG);
    }

    public static RequestValidationException noReadPermission() {
        return new RequestValidationException(NO_READ_ACCESS_CODE, NO_READ_ACCESS_MSG);
    }

    public static RequestValidationException noWritePermission() {
        return new RequestValidationException(NO_WRITE_PERM_CODE, NO_WRITE_PERM_MSG);
    }

    public static RequestValidationException notLoggedIn() {
        return new RequestValidationException(NOT_LOGGED_IN_CODE, NOT_LOGGED_IN_MSG);
    }

    public static RequestValidationException notAFile(String path) {
        String msg = MessageFormat.format(NOT_A_FILE_DOWNLOAD_MSG, path);
        return new RequestValidationException(NOT_A_FILE_DOWNLOAD_CODE, msg);
    }
}
