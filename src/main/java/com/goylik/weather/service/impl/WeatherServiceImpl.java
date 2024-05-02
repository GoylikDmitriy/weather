package com.goylik.weather.service.impl;

import com.goylik.weather.model.dto.AvgTempResponse;
import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.model.entity.Weather;
import com.goylik.weather.model.mapper.WeatherMapper;
import com.goylik.weather.repository.WeatherRepository;
import com.goylik.weather.service.WeatherService;
import com.goylik.weather.service.exception.WeatherFetchException;
import com.goylik.weather.service.exception.WeatherNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the WeatherService interface to manage weather-related operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;

    @Value("${weather-api.api-key}")
    private String apiKey;
    @Value("${weather-api.location}")
    private String location;

    @Scheduled(fixedRateString = "${weather-api.fixed-rate}", initialDelayString = "${weather-api.initial-delay}")
    void updateWeather() {
        log.info("Scheduled updating weather for location: {}", location);
        String weatherData = this.fetchWeather(location);
        log.info("Fetched weather data for location: {}", location);
        this.convertAndSave(weatherData);
        log.info("Weather was updated by schedule for location: {}", location);
    }

    private String fetchWeather(String location) {
        log.info("Fetching weather for location: {}", location);
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
                log.error("Failed to fetch weather data for location: {}. Status code: {}", location, response.statusCode());
                throw new WeatherFetchException("Failed to fetch weather data. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to fetch weather data for location: {}. Error: {}", location, e.getMessage());
            throw new WeatherFetchException("Failed to fetch weather data: " + e.getMessage());
        }
    }

    private void convertAndSave(String weatherJSON) {
        log.info("Trying to convert from JSON to Object. JSON: {}", weatherJSON);
        WeatherDto weatherDto = this.weatherMapper.mapToDtoFromJSON(weatherJSON);
        Long weatherId = this.saveWeather(weatherDto);
        weatherDto.setId(weatherId);
    }

    public WeatherDto getCurrentWeatherBySpecifiedLocation() {
        log.info("Trying to get current weather for specified location: {}", location);
        return this.getCurrentWeatherByLocation(location);
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherDto getCurrentWeatherByLocation(String location) throws WeatherNotFoundException {
        log.info("Trying to get current weather for location: {}", location);
        Weather weather = weatherRepository.findFirstByLocationOrderByDateDesc(location)
                .orElseThrow(() -> {
                    log.error("Can't find weather for location: {} ", location);
                    return new WeatherNotFoundException("Can't find weather for this location: " + location);
                });

        log.info("Retrieved weather for location: {}", location);
        return this.weatherMapper.map(weather);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeatherDto> getWeatherInDateRange(LocalDate from, LocalDate to) {
        log.info("Trying to get weather in date range: from {} to {}.", from, to);
        List<Weather> weathers = this.weatherRepository.findWeatherInDateRange(from, to);
        log.info("Retrieved weathers in date range: from {} to {}.", from, to);
        return weathers.stream()
                .map(weatherMapper::map)
                .toList();
    }

    @Override
    public AvgTempResponse getAverageDailyTemperature(LocalDate from, LocalDate to) {
        log.info("Trying to get average daily temp.");
        List<WeatherDto> weathers = this.getWeatherInDateRange(from, to);
        if (weathers.isEmpty()) {
            log.warn("Couldn't find any weather by these dates.");
            throw new WeatherNotFoundException("Couldn't find any weather by these dates.");
        }

        Double avg = weathers.stream()
                .mapToDouble(WeatherDto::getTemperature)
                .average()
                .orElse(0d);

        log.info("Average temperature is {} in date range: from {} to {}", avg, from, to);
        return new AvgTempResponse(avg);
    }

    @Override
    @Transactional
    public Long saveWeather(WeatherDto weatherDto) {
        log.info("Trying to save weather.");
        Weather weather = this.weatherMapper.map(weatherDto);
        weather = this.weatherRepository.save(weather);
        log.info("Weather was saved with id = {}", weather.getId());
        return weather.getId();
    }

    @Override
    @Transactional
    public Long deleteWeather(WeatherDto weatherDto) {
        log.info("Trying to delete weather with id = {}", weatherDto.getId());
        Weather weather = this.weatherMapper.map(weatherDto);
        this.weatherRepository.delete(weather);
        log.info("Weather was deleted.");
        return weather.getId();
    }

    @Override
    @Transactional
    public Long updateWeather(WeatherDto weatherDto) {
        log.info("Trying to update weather with id = {}", weatherDto.getId());
        Weather weather = this.weatherMapper.map(weatherDto);
        weather = this.weatherRepository.save(weather);
        log.info("Weather was updated with id = {}", weather.getId());
        return weather.getId();
    }
}