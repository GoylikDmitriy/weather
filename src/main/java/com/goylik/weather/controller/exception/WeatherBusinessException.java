package com.goylik.weather.controller.exception;

public class WeatherBusinessException extends Exception {
    public WeatherBusinessException() {
    }

    public WeatherBusinessException(String message) {
        super(message);
    }

    public WeatherBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherBusinessException(Throwable cause) {
        super(cause);
    }
}
