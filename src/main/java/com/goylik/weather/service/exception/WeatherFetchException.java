package com.goylik.weather.service.exception;

public class WeatherFetchException extends RuntimeException {
    public WeatherFetchException() {
    }

    public WeatherFetchException(String message) {
        super(message);
    }

    public WeatherFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
