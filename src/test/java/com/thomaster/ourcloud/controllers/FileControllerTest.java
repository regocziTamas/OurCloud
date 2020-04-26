package com.thomaster.ourcloud.controllers;

import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(FileController.class)
class FileControllerTest {

    @Test
    void getFile() throws IOException {
        Tika tika = new Tika();

        String result = tika.detect(new File("shrek"));

        System.out.println(result);


    }
}