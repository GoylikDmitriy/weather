package com.goylik.weather.controller;

import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<WeatherDto> getWeather(@RequestParam String location) {
        return null;
    }

    @GetMapping("/average-daily-temperature")
    public ResponseEntity<?> getAverageDailyTemperature(@RequestParam LocalDateTime from,
                                                        @RequestParam LocalDateTime to) {
        return null;
    }
}
