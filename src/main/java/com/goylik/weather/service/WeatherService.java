package com.goylik.weather.service;

import com.goylik.weather.model.dto.WeatherDto;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherService {
    WeatherDto getWeatherByLocation(String location);
    WeatherDto getWeatherByLocationAndDateTime(String location, LocalDateTime dateTime);
    List<WeatherDto> getWeatherInDateRange(LocalDateTime from, LocalDateTime to);
    Double getAverageDailyTemperature(LocalDateTime from, LocalDateTime to);
    Long saveWeather(WeatherDto weatherDto);
    Long deleteWeather(WeatherDto weatherDto);
    Long updateWeather(WeatherDto weatherDto);
}