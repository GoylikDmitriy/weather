package com.goylik.weather.controller.advice;

import com.goylik.weather.controller.exception.WeatherBusinessException;
import com.goylik.weather.model.dto.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling WeatherBusinessException.
 */
@ControllerAdvice
public class WeatherAdvice {
    /**
     * Handles WeatherBusinessException and returns an appropriate response with error message.
     *
     * @param e the WeatherBusinessException to handle.
     * @return a ResponseEntity containing the exception message.
     */
    @ExceptionHandler(WeatherBusinessException.class)
    public ResponseEntity<?> handleException(WeatherBusinessException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(e.getMessage());
        return ResponseEntity.ok(response);
    }
}
