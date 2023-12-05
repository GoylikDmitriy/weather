package com.goylik.weather.service;

import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.service.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeatherService {
    Optional<WeatherDto> getCurrentWeatherBySpecifiedLocation() throws ServiceException;
    Optional<WeatherDto> getCurrentWeatherByLocation(String location) throws ServiceException;
    List<WeatherDto> getWeatherInDateRange(LocalDateTime from, LocalDateTime to) throws ServiceException;
    Double getAverageDailyTemperature(LocalDateTime from, LocalDateTime to) throws ServiceException;
    Long saveWeather(WeatherDto weatherDto) throws ServiceException;
    Long deleteWeather(WeatherDto weatherDto) throws ServiceException;
    Long updateWeather(WeatherDto weatherDto) throws ServiceException;
}