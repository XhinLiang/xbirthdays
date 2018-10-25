package com.xhinliang.birthdays.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.xhinliang.birthdays.common.dto.NormalResponse;

/**
 * ControllerAdvice tells your spring application that this class
 * will do the exception handling for your application.
 * <p>
 * RestController will make it a controller and let this class render the response.
 * Use @ExceptionHandler annotation to define the class of Exception it will catch.
 * <p>
 * (A Base class will catch all the Inherited and extended classes)
 **/
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Throwable.class)
    public NormalResponse handleThrowable(Throwable e) {
        NormalResponse response = NormalResponse.ofThrowable(e);
        logger.error("unhandled throwable", e);
        return response;
    }
}
