package com.goylik.weather.service.impl;

import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.model.entity.Weather;
import com.goylik.weather.model.mapper.WeatherMapper;
import com.goylik.weather.model.mapper.exception.JsonMappingException;
import com.goylik.weather.model.mapper.exception.MappingException;
import com.goylik.weather.repository.WeatherRepository;
import com.goylik.weather.service.WeatherService;
import com.goylik.weather.service.exception.WeatherServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the WeatherService interface to manage weather-related operations.
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
    private WeatherRepository weatherRepository;
    private WeatherMapper weatherMapper;

    @Value("${weather-api.api-key}")
    private String apiKey;
    @Value("${weather-api.location}")
    private String location;

    @Autowired
    public WeatherServiceImpl(WeatherRepository weatherRepository, WeatherMapper weatherMapper) {
        this.weatherRepository = weatherRepository;
        this.weatherMapper = weatherMapper;
    }

    @Scheduled(fixedRateString = "${weather-api.fixed-rate}", initialDelayString = "${weather-api.initial-delay}")
    void updateWeather() {
        logger.info("Scheduled updating weather for location: {}", location);
        String weatherData = this.fetchWeather(location);
        logger.info("Fetched weather data for location: {}", location);
        this.convertAndSave(weatherData);
        logger.info("Weather was updated by schedule for location: {}", location);
    }

    private String fetchWeather(String location) {
        logger.info("Fetching weather for location: {}", location);
        String weatherApiUrl = "https://weatherapi-com.p.rapidapi.com/current.json?q=" + location;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(weatherApiUrl))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                logger.error("Failed to fetch weather data for location: {}. Status code: {}", location, response.statusCode());
                throw new RuntimeException("Failed to fetch weather data. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Failed to fetch weather data for location: {}. Error: {}", location, e.getMessage());
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
        }
    }

    private void convertAndSave(String weatherJSON) {
        try {
            logger.info("Trying to convert from JSON to Object. JSON: {}", weatherJSON);
            WeatherDto weatherDto = this.weatherMapper.mapToDtoFromJSON(weatherJSON);
            Long weatherId = this.saveWeather(weatherDto);
            weatherDto.setId(weatherId);
        }
        catch (JsonMappingException e) {
            logger.error("Can't convert JSON string to Object: {}", e.getMessage());
            throw new RuntimeException("Can't convert JSON string to Object: " + e.getMessage());
        }
        catch (WeatherServiceException e) {
            logger.error("Can't save weather: {}", e.getMessage());
            throw new RuntimeException("Can't save weather: " + e.getMessage());
        }
    }

    public WeatherDto getCurrentWeatherBySpecifiedLocation() throws WeatherServiceException {
        logger.info("Trying to get current weather for specified location: {}", location);
        return this.getCurrentWeatherByLocation(location);
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherDto getCurrentWeatherByLocation(String location) throws WeatherServiceException {
        try {
            logger.info("Trying to get current weather for location: {}", location);
            Optional<Weather> weatherOptional = this.weatherRepository.findFirstByLocationOrderByDateTimeDesc(location);
            if (weatherOptional.isEmpty()) {
                logger.error("Can't find weather for location: {} ", location);
                throw new WeatherServiceException("Can't find weather for this location: " + location);
            }

            Weather weather = weatherOptional.get();
            logger.info("Retrieved weather for location: {}", location);
            return this.weatherMapper.map(weather);
        }
        catch (MappingException e) {
            logger.error("Error while getting weather by location: {}. Error: {}", location, e.getMessage());
            throw new WeatherServiceException("Error while getting weather by location.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeatherDto> getWeatherInDateRange(LocalDateTime from, LocalDateTime to) throws WeatherServiceException {
        try {
            logger.info("Trying to get weather in date range: from {} to {}.", from, to);
            List<Weather> weathers = this.weatherRepository.findWeatherInDateRange(from, to);
            List<WeatherDto> weatherDtos = new ArrayList<>();
            for (Weather weather : weathers) {
                weatherDtos.add(this.weatherMapper.map(weather));
            }

            logger.info("Retrieved weathers in date range: from {} to {}.", from, to);
            return weatherDtos;
        }
        catch (MappingException e) {
            logger.error("Error while getting weather in date range: from {} to {}. Error: {}", from, to, e.getMessage());
            throw new WeatherServiceException("Error while getting weather in date range.", e);
        }
    }

    @Override
    public Double getAverageDailyTemperature(LocalDateTime from, LocalDateTime to) throws WeatherServiceException {
        logger.info("Trying to get average daily temp.");
        List<WeatherDto> weathers = this.getWeatherInDateRange(from, to);
        if (weathers.size() == 0) {
            logger.error("Couldn't find any weather by these dates.");
            throw new WeatherServiceException("Couldn't find any weather by these dates.");
        }

        double averageTemperature = 0d;
        for (WeatherDto weather : weathers) {
            averageTemperature += weather.getTemperature();
        }

        averageTemperature /= weathers.size();
        logger.info("Retrieved average temperature.");
        return averageTemperature;
    }

    @Override
    @Transactional
    public Long saveWeather(WeatherDto weatherDto) throws WeatherServiceException {
        try {
            logger.info("Trying to save weather.");
            Weather weather = this.weatherMapper.map(weatherDto);
            weather = this.weatherRepository.save(weather);
            logger.info("Weather was saved with id = {}", weather.getId());
            return weather.getId();
        }
        catch (MappingException e) {
            logger.error("Error while saving weather. Error: {}", e.getMessage());
            throw new WeatherServiceException("Error while saving weather.", e);
        }
    }

    @Override
    @Transactional
    public Long deleteWeather(WeatherDto weatherDto) throws WeatherServiceException {
        try {
            logger.info("Trying to delete weather with id = {}", weatherDto.getId());
            Weather weather = this.weatherMapper.map(weatherDto);
            this.weatherRepository.delete(weather);
            logger.info("Weather was deleted.");
            return weather.getId();
        }
        catch (MappingException e) {
            logger.error("Error while deleting weather. Error: {}", e.getMessage());
            throw new WeatherServiceException("Error while deleting weather.", e);
        }
    }

    @Override
    @Transactional
    public Long updateWeather(WeatherDto weatherDto) throws WeatherServiceException {
        try {
            logger.info("Trying to update weather with id = {}", weatherDto.getId());
            Weather weather = this.weatherMapper.map(weatherDto);
            weather = this.weatherRepository.save(weather);
            logger.info("Weather was updated with id = {}", weather.getId());
            return weather.getId();
        }
        catch (MappingException e) {
            logger.error("Error while updating weather. Error: {}", e.getMessage());
            throw new WeatherServiceException("Error while updating weather.", e);
        }
    }
}