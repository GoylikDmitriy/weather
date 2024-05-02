package com.goylik.weather.controller;

import com.goylik.weather.model.dto.AvgTempResponse;
import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.service.WeatherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Controller for handling weather-related requests.
 */
@RestController
@RequestMapping("/weather")
@AllArgsConstructor
@Slf4j
public class WeatherController {
    private WeatherService weatherService;

    @GetMapping("/current")
    public WeatherDto getCurrentWeather() {
        log.info("Trying to get current weather by GET method.");
        return this.weatherService.getCurrentWeatherBySpecifiedLocation();
    }

    @GetMapping("/avg-temp")
    public AvgTempResponse getAverageDailyTemperature(@RequestParam("from")
                                                      @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
                                                      @RequestParam("to")
                                                      @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to) {
        log.info("Getting average daily temperature by POST method.");
        return this.weatherService.getAverageDailyTemperature(from, to);
    }
}
