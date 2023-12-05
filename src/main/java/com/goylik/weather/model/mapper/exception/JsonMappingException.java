package com.goylik.weather.model.mapper.exception;

public class JsonMappingException extends MappingException {
    public JsonMappingException() {
    }

    public JsonMappingException(String message) {
        super(message);
    }

    public JsonMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonMappingException(Throwable cause) {
        super(cause);
    }
}
