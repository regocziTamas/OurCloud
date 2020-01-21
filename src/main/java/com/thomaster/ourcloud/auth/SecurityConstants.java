package com.thomaster.ourcloud.auth;

public interface SecurityConstants {

    String AUTH_LOGIN_URL = "/login";

    String JWT_SECRET = System.getenv("JWT_SECRET");

    String TOKEN_HEADER = "Authorization";
    String TOKEN_PREFIX = "Bearer ";
    String TOKEN_TYPE = "JWT";
    String TOKEN_ISSUER = "ourcloud-api";
    String TOKEN_AUDIENCE = "ourcloud-app";

}
