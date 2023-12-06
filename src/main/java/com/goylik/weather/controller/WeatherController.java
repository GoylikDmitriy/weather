package com.goylik.weather.controller;

import com.goylik.weather.controller.exception.WeatherBusinessException;
import com.goylik.weather.model.dto.AverageTemperatureResponse;
import com.goylik.weather.model.dto.DateRangeRequest;
import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.service.WeatherService;
import com.goylik.weather.service.exception.ServiceException;
import com.goylik.weather.service.impl.WeatherServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for handling weather-related requests.
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Handles a GET request to retrieve the current weather.
     *
     * @return a ResponseEntity object containing information about the current weather.
     * @throws WeatherBusinessException if there are issues handling the request.
     */
    @GetMapping("/current")
    public ResponseEntity<WeatherDto> getCurrentWeather() throws WeatherBusinessException {
        try {
            logger.info("Trying to get current weather by GET method.");
            Optional<WeatherDto> weatherDtoOptional = this.weatherService.getCurrentWeatherBySpecifiedLocation();
            ResponseEntity<WeatherDto> weatherDtoResponse = weatherDtoOptional.map(ResponseEntity::ok)
                    .orElseThrow(() -> {
                        logger.error("Can't find weather for specified location.");
                        return new WeatherBusinessException("Can't find weather for specified location.");
                    });

            logger.info("Retrieved current weather.");
            return weatherDtoResponse;
        }
        catch (ServiceException e) {
            logger.error("Error while getting weather by GET method: {}", e.getMessage());
            throw new WeatherBusinessException(e.getMessage());
        }
    }

    /**
     * Handles a POST request to retrieve the average daily temperature for the specified period.
     *
     * @param request        the object containing the date range.
     * @param bindingResult  the result of validating the request.
     * @return a ResponseEntity object containing the average temperature for the specified period.
     * @throws WeatherBusinessException if there are issues handling the request.
     */
    @PostMapping("/average-daily-temperature")
    public ResponseEntity<?> getAverageDailyTemperature(@RequestBody @Valid DateRangeRequest request,
                                                        BindingResult bindingResult) throws WeatherBusinessException {
        logger.info("Getting average daily temperature by POST method.");
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("; "));

            logger.error("Error in request: {}", errorMessage);
            throw new WeatherBusinessException(errorMessage);
        }

        try {
            LocalDateTime from = LocalDate.parse(request.getFrom(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay();
            LocalDateTime to = LocalDate.parse(request.getTo(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).plusDays(1).atStartOfDay();
            Double averageTemperature = this.weatherService.getAverageDailyTemperature(from, to);

            AverageTemperatureResponse response = new AverageTemperatureResponse();
            response.setAverageTemp(averageTemperature);
            logger.info("Retrieved average temperature = {}", averageTemperature);
            return ResponseEntity.ok(response);
        }
        catch (ServiceException e) {
            logger.error("Error while getting average temperature by POST method: {}", e.getMessage());
            throw new WeatherBusinessException(e.getMessage());
        }
    }
}
