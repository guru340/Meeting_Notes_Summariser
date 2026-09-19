package com.example.Meeting_Notes_Summariser.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static reactor.netty.http.HttpConnectionLiveness.log;

@ControllerAdvice
public class GlobalException {

    @ResponseBody
    @ExceptionHandler(value={Exception.class})
    public ErrorDTO handleException(Exception exception){
        log.error("Exception occured: {}",exception.getMessage(),exception);
        return new ErrorDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(),exception.getMessage());

    }
}
