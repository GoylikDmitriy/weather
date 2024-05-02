package com.goylik.weather.controller.advice;

import com.goylik.weather.model.dto.ErrorResponse;
import com.goylik.weather.service.exception.WeatherFetchException;
import com.goylik.weather.service.exception.WeatherNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling WeatherBusinessException.
 */
@RestControllerAdvice
@Slf4j
public class WeatherAdvice {
    @ExceptionHandler(WeatherNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWeatherNotFoundException(WeatherNotFoundException ex) {
        log.error("NOT FOUND: {}", ex.getMessage(), ex);
        return new ErrorResponse("Not Found", 404, ex.getMessage());
    }

    @ExceptionHandler(WeatherFetchException.class)
    public ErrorResponse handleWeatherFetchException(WeatherFetchException ex) {
        log.error("API SERVER ERROR: {}", ex.getMessage());
        return new ErrorResponse("API SERVER ERROR", 500, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public ErrorResponse handleDataAccessException(DataAccessException ex) {
        log.error("DATA ACCESS ERROR: {}", ex.getMessage(), ex);
        return new ErrorResponse("Internal Server Error", 500,
                "An error occurred while accessing the data: " + ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        log.error("INTERNAL SERVER ERROR: {}", ex.getMessage(), ex);
        return new ErrorResponse("Internal Server Error", 500, ex.getMessage());
    }
}
