package com.thomaster.ourcloud.services.request;

import com.thomaster.ourcloud.services.request.base.BaseRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class RequestValidator<T extends BaseRequest> {

    protected List<Consumer<T>> validationElements = new ArrayList<>();

    public RequestValidator() {

    }

    public final void validateRequest(T request) {
        validationElements
                .forEach(el -> el.accept(request));
    }

}
