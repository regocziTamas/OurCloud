package com.thomaster.ourcloud.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class ReqBodyToUsernamePwd {
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    private String parsedUsername;
    private String parsedPassword;

    public ReqBodyToUsernamePwd(HttpServletRequest loginAttemptRequest)
    {
        String requestBody = null;

        try {
            requestBody = loginAttemptRequest.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(requestBody != null) {
            try {
                Map<String, String> parsedLoginRequestBody = new ObjectMapper().readValue(requestBody, Map.class);
                parsedUsername = parsedLoginRequestBody.get(USERNAME_KEY);
                parsedPassword = parsedLoginRequestBody.get(PASSWORD_KEY);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public String getParsedUsername() {
        return parsedUsername;
    }

    public String getParsedPassword() {
        return parsedPassword;
    }
}
