package com.simulator.location.advice;

import lombok.extern.flogger.Flogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Flogger
@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        log.atSevere().log(e.getMessage());
        return new ResponseEntity<>("Internal Server Error. \n Please try again Later!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
