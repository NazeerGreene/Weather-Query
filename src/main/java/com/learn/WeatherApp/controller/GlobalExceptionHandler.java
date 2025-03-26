package com.learn.WeatherApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public static ResponseEntity<String> tooManyRequestsException(HttpClientErrorException ignored) {
        logger.error("Rate limit exceeded for weather API");

        return ResponseEntity.status(429) // too many requests
                .body("Rate limit exceeded - try again later!");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public static ResponseEntity<String> wrongEndpoint(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid endpoint: " + ex.getResourcePath());
    }

    @ExceptionHandler(Exception.class)
    public static ResponseEntity<String> generalException(Exception e) {
        logger.error("Uncaught exception: {}", e.getMessage());
        e.printStackTrace();

        return ResponseEntity.status(500)
                .body("Oh no! Something went wrong, check back later!");
    }

}
