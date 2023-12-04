package com.goylik.weather.service;

import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.model.entity.Weather;
import com.goylik.weather.model.mapper.WeatherMapper;
import com.goylik.weather.repository.WeatherRepository;
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
import java.util.List;
import java.util.stream.Collectors;

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

    public void convertAndSave(String weatherJSON) {
        WeatherDto weatherDto = this.weatherMapper.mapToDtoFromJSON(weatherJSON);
        Long weatherId = this.saveWeather(weatherDto);
        weatherDto.setId(weatherId);
    }

    @Override
    public WeatherDto getWeatherByLocation(String location) {
        Weather weather = this.weatherRepository.findWeatherByLocation(location);
        return this.weatherMapper.map(weather);
    }

    @Override
    public WeatherDto getWeatherByLocationAndDateTime(String location, LocalDateTime dateTime) {
        Weather weather = this.weatherRepository.findWeatherByLocationAndDateTime(location, dateTime);
        return this.weatherMapper.map(weather);
    }

    @Override
    public List<WeatherDto> getWeatherInDateRange(LocalDateTime from, LocalDateTime to) {
        List<Weather> weathers = this.weatherRepository.findWeatherInDateRange(from, to);
        return weathers.stream().map(this.weatherMapper::map).collect(Collectors.toList());
    }

    @Override
    public Double getAverageDailyTemperature(LocalDateTime from, LocalDateTime to) {
        List<WeatherDto> weathers = this.getWeatherInDateRange(from, to);
        Double averageTemperature = 0d;
        for (WeatherDto weather : weathers) {
            averageTemperature += weather.getTemperature();
        }

        averageTemperature /= weathers.size();
        return averageTemperature;
    }

    @Override
    public Long saveWeather(WeatherDto weatherDto) {
        Weather weather = this.weatherMapper.map(weatherDto);
        weather = this.weatherRepository.save(weather);
        return weather.getId();
    }

    @Override
    public Long deleteWeather(WeatherDto weatherDto) {
        Weather weather = this.weatherMapper.map(weatherDto);
        this.weatherRepository.delete(weather);
        return weather.getId();
    }

    @Override
    public Long updateWeather(WeatherDto weatherDto) {
        Weather weather = this.weatherMapper.map(weatherDto);
        weather = this.weatherRepository.save(weather);
        return weather.getId();
    }
}