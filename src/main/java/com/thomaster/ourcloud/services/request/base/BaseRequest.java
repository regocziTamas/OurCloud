package com.thomaster.ourcloud.services.request.base;

import com.thomaster.ourcloud.model.user.OCUser;

import java.util.Optional;

public abstract class BaseRequest {

    private OCUser initiatingUser;

    protected BaseRequest(OCUser initiatingUser) {
        this.initiatingUser = initiatingUser;
    }

    public Optional<OCUser> getInitiatingUser() {
        return Optional.ofNullable(initiatingUser);
    }

    public static abstract class BaseRequestBuilder<T extends BaseRequest> {

        protected OCUser initiatingUser;

        public BaseRequestBuilder() {
        }

        public BaseRequestBuilder<T> initiatingUser(OCUser initiatingUser) {
            this.initiatingUser = initiatingUser;
            return this;
        }

        public abstract T build();
    }
}
