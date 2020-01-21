package com.thomaster.ourcloud.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    @GetMapping("/file")
    public String fileController()
    {
        return "shit";
    }

}
