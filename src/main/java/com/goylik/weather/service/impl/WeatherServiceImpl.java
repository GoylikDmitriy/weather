package com.goylik.weather.service.impl;

import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.model.entity.Weather;
import com.goylik.weather.model.mapper.WeatherMapper;
import com.goylik.weather.model.mapper.exception.JsonMappingException;
import com.goylik.weather.model.mapper.exception.MappingException;
import com.goylik.weather.repository.WeatherRepository;
import com.goylik.weather.service.WeatherService;
import com.goylik.weather.service.exception.WeatherServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {
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

    @Scheduled(fixedRateString = "${weather-api.rate}")
    private void updateWeather() {
        String weatherData = this.fetchWeather(location);
        this.convertAndSave(weatherData);
    }

    private String fetchWeather(String location) {
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
                throw new RuntimeException("Failed to fetch weather data. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
        }
    }

    private void convertAndSave(String weatherJSON) {
        try {
            WeatherDto weatherDto = this.weatherMapper.mapToDtoFromJSON(weatherJSON);
            Long weatherId = this.saveWeather(weatherDto);
            weatherDto.setId(weatherId);
        }
        catch (JsonMappingException e) {
            throw new RuntimeException("Can't convert JSON string to Object: " + e.getMessage());
        }
        catch (WeatherServiceException e) {
            throw new RuntimeException("Can't save weather: " + e.getMessage());
        }
    }

    public Optional<WeatherDto> getCurrentWeatherBySpecifiedLocation() throws WeatherServiceException {
        try {
            return this.getCurrentWeatherByLocation(location);
        }
        catch (WeatherServiceException e) {
            throw new WeatherServiceException("Error while getting current weather by specified location.", e);
        }
    }

    @Override
    public Optional<WeatherDto> getCurrentWeatherByLocation(String location) throws WeatherServiceException {
        try {
            Optional<Weather> weatherOptional = this.weatherRepository.findFirstByLocationOrderByDateTimeDesc(location);
            if (weatherOptional.isEmpty()) {
                throw new WeatherServiceException("Can't find weather for this location: " + location);
            }

            Weather weather = weatherOptional.get();
            return Optional.of(this.weatherMapper.map(weather));
        }
        catch (MappingException e) {
            throw new WeatherServiceException("Error while getting weather by location.", e);
        }
    }

    @Override
    public List<WeatherDto> getWeatherInDateRange(LocalDateTime from, LocalDateTime to) throws WeatherServiceException {
        try {
            List<Weather> weathers = this.weatherRepository.findWeatherInDateRange(from, to);
            List<WeatherDto> weatherDtos = new ArrayList<>();
            for (Weather weather : weathers) {
                weatherDtos.add(this.weatherMapper.map(weather));
            }

            return weatherDtos;
        }
        catch (MappingException e) {
            throw new WeatherServiceException("Error while getting weather in date range.", e);
        }
    }

    @Override
    public Double getAverageDailyTemperature(LocalDateTime from, LocalDateTime to) throws WeatherServiceException {
        try {
            List<WeatherDto> weathers = this.getWeatherInDateRange(from, to);
            if (weathers.size() == 0) {
                throw new WeatherServiceException("Couldn't find any weather by these dates.");
            }

            double averageTemperature = 0d;
            for (WeatherDto weather : weathers) {
                averageTemperature += weather.getTemperature();
            }

            averageTemperature /= weathers.size();
            return averageTemperature;
        }
        catch (WeatherServiceException e) {
            throw new WeatherServiceException("Error while getting average daily temperature.", e);
        }
    }

    @Override
    public Long saveWeather(WeatherDto weatherDto) throws WeatherServiceException {
        try {
            Weather weather = this.weatherMapper.map(weatherDto);
            weather = this.weatherRepository.save(weather);
            return weather.getId();
        }
        catch (MappingException e) {
            throw new WeatherServiceException("Error while saving weather.", e);
        }
    }

    @Override
    public Long deleteWeather(WeatherDto weatherDto) throws WeatherServiceException {
        try {
            Weather weather = this.weatherMapper.map(weatherDto);
            this.weatherRepository.delete(weather);
            return weather.getId();
        }
        catch (MappingException e) {
            throw new WeatherServiceException("Error while deleting weather.", e);
        }
    }

    @Override
    public Long updateWeather(WeatherDto weatherDto) throws WeatherServiceException {
        try {
            Weather weather = this.weatherMapper.map(weatherDto);
            weather = this.weatherRepository.save(weather);
            return weather.getId();
        }
        catch (MappingException e) {
            throw new WeatherServiceException("Error while updating weather.", e);
        }
    }
}