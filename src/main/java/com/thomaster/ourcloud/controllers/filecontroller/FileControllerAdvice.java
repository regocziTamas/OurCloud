package com.thomaster.ourcloud.controllers.filecontroller;

import com.thomaster.ourcloud.services.request.RequestValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {FileController.class})
public class FileControllerAdvice {


    @ExceptionHandler({RequestValidationException.class})
    public ResponseEntity<String> handlerRequestValidationException(Exception ex) {
        RequestValidationException reqValException = (RequestValidationException) ex;

        System.out.println("Caught exception: " + reqValException.getErrorCode() + " " + reqValException.getErrorMsg());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header("errorCode", reqValException.getErrorCode())
                .body(reqValException.getErrorMsg());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<String> handleMissingServlerParameterException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Bad Request");
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        System.out.println("Exception caught!");
        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Error");
    }
}
