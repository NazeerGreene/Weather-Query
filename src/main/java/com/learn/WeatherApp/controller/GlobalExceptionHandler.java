package com.learn.WeatherApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public static ResponseEntity<String> tooManyRequestsException(HttpClientErrorException ignored) {
        logger.error("Rate limit exceeded for weather API");

        return ResponseEntity.status(429) // too many requests
                .body("Rate limit exceeded - try again later!");
    }

    @ExceptionHandler(Exception.class)
    public static ResponseEntity<String> generalException(Exception e) {
        logger.error("Uncaught exception: {}", e.getMessage());

        return ResponseEntity.status(500)
                .body("Oh no! Something went wrong, check back later!");
    }

}
