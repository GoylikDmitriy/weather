package com.goylik.weather.model.mapper.exception;

public class WeatherMappingException extends MappingException {
    public WeatherMappingException() {
    }

    public WeatherMappingException(String message) {
        super(message);
    }

    public WeatherMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherMappingException(Throwable cause) {
        super(cause);
    }
}
