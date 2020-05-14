package com.thomaster.ourcloud.services;

import com.thomaster.ourcloud.services.request.save.file.PreFlightSaveFileRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PreFlightSaveFileRequestTokenService {

    private Map<String, PreFlightSaveFileRequest> tokenRepository = new HashMap<>();

    public String getTokenForPreFlightRequest(PreFlightSaveFileRequest request) {
        String newToken = UUID.randomUUID().toString();

        tokenRepository.put(newToken, request);

        return newToken;
    }

    public PreFlightSaveFileRequest getRequestForToken(String token) {
        return tokenRepository.get(token);
    }

    public void removeToken(String token) {
        tokenRepository.remove(token);
    }

}
