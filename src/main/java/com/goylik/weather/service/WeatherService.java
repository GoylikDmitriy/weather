package com.goylik.weather.service;

import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.service.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing weather-related operations.
 */
public interface WeatherService {
    /**
     * Retrieves the current weather for location that specified in properties.
     *
     * @return an Optional containing the current weather as a WeatherDto, or an empty Optional if the weather is not available
     * @throws ServiceException if there are issues retrieving the current weather
     */
    WeatherDto getCurrentWeatherBySpecifiedLocation() throws ServiceException;

    /**
     * Retrieves the current weather for a specific location.
     *
     * @param location the location for which to retrieve the current weather
     * @return an Optional containing the current weather as a WeatherDto, or an empty Optional if the weather is not available.
     * @throws ServiceException if there are issues retrieving the current weather.
     */
    WeatherDto getCurrentWeatherByLocation(String location) throws ServiceException;

    /**
     * Retrieves the weather data within the specified date range.
     *
     * @param from the start date of the date range.
     * @param to the end date of the date range.
     * @return a list of WeatherDto objects within the specified date range.
     * @throws ServiceException if there are issues retrieving the weather data.
     */
    List<WeatherDto> getWeatherInDateRange(LocalDateTime from, LocalDateTime to) throws ServiceException;

    /**
     * Calculates the average daily temperature within the specified date range.
     *
     * @param from the start date of the date range.
     * @param to the end date of the date range.
     * @return the average daily temperature within the specified date range.
     * @throws ServiceException if there are issues calculating the average daily temperature.
     */
    Double getAverageDailyTemperature(LocalDateTime from, LocalDateTime to) throws ServiceException;

    /**
     * Saves the weather.
     *
     * @param weatherDto the WeatherDto object containing the weather to be saved.
     * @return the ID of the saved weather record.
     * @throws ServiceException if there are issues saving the weather.
     */
    Long saveWeather(WeatherDto weatherDto) throws ServiceException;

    /**
     * Deletes the weather.
     *
     * @param weatherDto the WeatherDto object containing the weather to be deleted.
     * @return the ID of the deleted weather record.
     * @throws ServiceException if there are issues deleting the weather.
     */
    Long deleteWeather(WeatherDto weatherDto) throws ServiceException;

    /**
     * Updates the weather.
     *
     * @param weatherDto the WeatherDto object containing the updated weather.
     * @return the ID of the updated weather record.
     * @throws ServiceException if there are issues updating the weather.
     */
    Long updateWeather(WeatherDto weatherDto) throws ServiceException;
}