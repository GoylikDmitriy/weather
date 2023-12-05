package com.goylik.weather.service.exception;

public class WeatherServiceException extends ServiceException {
    public WeatherServiceException() {
    }

    public WeatherServiceException(String message) {
        super(message);
    }

    public WeatherServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherServiceException(Throwable cause) {
        super(cause);
    }
}
