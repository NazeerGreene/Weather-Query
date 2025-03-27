package com.learn.WeatherApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for handling application-wide exceptions.
 *
 * This class provides centralized exception handling for all controllers
 * by using {@link ExceptionHandler} methods to return meaningful HTTP responses.
 * It helps in maintaining a consistent error response structure across the application.
 *
 * The following exceptions are handled:
 *   {@link HttpClientErrorException.TooManyRequests} - Rate limiting errors</li>
 *   {@link NoResourceFoundException} - Invalid API endpoint errors</li>
 *   {@link Exception} - All uncaught exceptions</li>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles rate limit errors from the external weather API.
     *
     * This method catches {@link HttpClientErrorException.TooManyRequests} exceptions,
     * which occur when too many requests are made within a short period.
     * It logs the error and returns a {@code 429 Too Many Requests} response.
     *
     * @param ignored The exception (ignored in logging, as the message is static).
     * @return A {@link ResponseEntity} with status 429 (Too Many Requests) and an error message.
     */
    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public static ResponseEntity<String> tooManyRequestsException(HttpClientErrorException ignored) {
        logger.error("Rate limit exceeded for weather API");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Rate limit exceeded - try again later!");
    }

    /**
     * Handles requests to invalid API endpoints.
     *
     * This method catches {@link NoResourceFoundException} when a user tries
     * to access an endpoint that does not exist. It returns a
     * {@code 400 Bad Request} response along with the invalid path.
     *
     * @param ex The exception containing details of the incorrect resource.
     * @return A {@link ResponseEntity} with status 400 (Bad Request) and an error message.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public static ResponseEntity<String> wrongEndpoint(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid endpoint: " + ex.getResourcePath());
    }

    /**
     * Handles all uncaught exceptions within the application.
     *
     * This method serves as a generic exception handler to catch any unexpected
     * errors in the system, log them, and return a {@code 500 Internal Server Error}
     * response to the user with a friendly message.
     *
     * @param e The unexpected exception.
     * @return A {@link ResponseEntity} with status 500 (Internal Server Error) and an error message.
     */
    @ExceptionHandler(Exception.class)
    public static ResponseEntity<String> generalException(Exception e) {
        logger.error("Uncaught exception: {}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Oh no! Something went wrong, check back later!");
    }
}
