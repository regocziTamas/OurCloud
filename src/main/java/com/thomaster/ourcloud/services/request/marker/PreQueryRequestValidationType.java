package com.thomaster.ourcloud.services.request.marker;

import com.thomaster.ourcloud.services.request.save.file.SaveFileRequestValidator;

import java.util.function.Consumer;

public enum PreQueryRequestValidationType {
    UPLOAD_FILE(),
    UPLOAD_FOLDER(),
    DELETE();


}
