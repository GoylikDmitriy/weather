package com.goylik.weather.controller;

import com.goylik.weather.controller.exception.WeatherBusinessException;
import com.goylik.weather.model.dto.AverageTemperatureResponse;
import com.goylik.weather.model.dto.DateRangeRequest;
import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.service.WeatherService;
import com.goylik.weather.service.exception.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<WeatherDto> getCurrentWeather() throws WeatherBusinessException {
        try {
            Optional<WeatherDto> weatherDtoOptional = this.weatherService.getCurrentWeatherBySpecifiedLocation();
            return weatherDtoOptional.map(ResponseEntity::ok)
                    .orElseThrow(() -> new WeatherBusinessException("Can't find weather for specified location."));
        }
        catch (ServiceException e) {
            throw new WeatherBusinessException(e.getMessage());
        }
    }

    @PostMapping("/average-daily-temperature")
    public ResponseEntity<?> getAverageDailyTemperature(@RequestBody @Valid DateRangeRequest request,
                                                        BindingResult bindingResult) throws WeatherBusinessException {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("; "));

            throw new WeatherBusinessException(errorMessage);
        }

        try {
            LocalDateTime from = LocalDateTime.parse(request.getFrom(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDateTime to = LocalDateTime.parse(request.getTo(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            Double averageTemperature = this.weatherService.getAverageDailyTemperature(from, to);

            AverageTemperatureResponse response = new AverageTemperatureResponse();
            response.setAverageTemp(averageTemperature);
            return ResponseEntity.ok(response);
        }
        catch (ServiceException e) {
            throw new WeatherBusinessException(e.getMessage());
        }
    }
}
