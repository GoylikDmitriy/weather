package com.goylik.weather.service;

import com.goylik.weather.model.dto.AvgTempResponse;
import com.goylik.weather.model.dto.WeatherDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing weather-related operations.
 */
public interface WeatherService {
    /**
     * Retrieves the current weather for location that specified in properties.
     *
     * @return an Optional containing the current weather as a WeatherDto, or an empty Optional if the weather is not available
     */
    WeatherDto getCurrentWeatherBySpecifiedLocation();

    /**
     * Retrieves the current weather for a specific location.
     *
     * @param location the location for which to retrieve the current weather
     * @return an Optional containing the current weather as a WeatherDto, or an empty Optional if the weather is not available.
     */
    WeatherDto getCurrentWeatherByLocation(String location);

    /**
     * Retrieves the weather data within the specified date range.
     *
     * @param from the start date of the date range.
     * @param to the end date of the date range.
     * @return a list of WeatherDto objects within the specified date range.
     */
    List<WeatherDto> getWeatherInDateRange(LocalDate from, LocalDate to);

    /**
     * Calculates the average daily temperature within the specified date range.
     *
     * @param from the start date of the date range.
     * @param to the end date of the date range.
     * @return the average daily temperature within the specified date range.
     */
    AvgTempResponse getAverageDailyTemperature(LocalDate from, LocalDate to);

    /**
     * Saves the weather.
     *
     * @param weatherDto the WeatherDto object containing the weather to be saved.
     * @return the ID of the saved weather record.
     */
    Long saveWeather(WeatherDto weatherDto);

    /**
     * Deletes the weather.
     *
     * @param weatherDto the WeatherDto object containing the weather to be deleted.
     * @return the ID of the deleted weather record.
     */
    Long deleteWeather(WeatherDto weatherDto);

    /**
     * Updates the weather.
     *
     * @param weatherDto the WeatherDto object containing the updated weather.
     * @return the ID of the updated weather record.
     */
    Long updateWeather(WeatherDto weatherDto);
}