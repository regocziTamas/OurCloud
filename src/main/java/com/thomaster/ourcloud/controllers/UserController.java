package com.thomaster.ourcloud.controllers;

import com.thomaster.ourcloud.json.UserJSON;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.thomaster.ourcloud.Constants.API_ENDPOINT_PREFIX;


@RestController
public class UserController {

    private OCUserService userService;

    public UserController(OCUserService userService) {
        this.userService = userService;
    }

    @GetMapping(API_ENDPOINT_PREFIX +"/user/current")
    public ResponseEntity<UserJSON> getUserDetails() {
        OCUser currentlyLoggedInUser = userService.getCurrentlyLoggedInUser()
                .orElseThrow(RequestValidationException::notLoggedIn);

        return ResponseEntity.ok()
                .body(new UserJSON(currentlyLoggedInUser));
    }
}
