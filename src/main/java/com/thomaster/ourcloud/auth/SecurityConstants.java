package com.thomaster.ourcloud.auth;

import static com.thomaster.ourcloud.Constants.API_ENDPOINT_PREFIX;

public interface SecurityConstants {

    String AUTH_LOGIN_URL = API_ENDPOINT_PREFIX + "/login";

    String JWT_SECRET = System.getenv("JWT_SECRET");

    String TOKEN_HEADER = "Authorization";
    String TOKEN_PREFIX = "Bearer ";
    String TOKEN_TYPE = "JWT";
    String TOKEN_ISSUER = "ourcloud-api";
    String TOKEN_AUDIENCE = "ourcloud-app";

}
