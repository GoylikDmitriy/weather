package com.goylik.weather.service;

import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
@Transactional
public class WeatherServiceTest {
    @Autowired
    WeatherService weatherService;

    WeatherDto weatherDto;

    @BeforeEach
    public void setup() {
        weatherDto = new WeatherDto();
        weatherDto.setTemperature(4.3d);
        weatherDto.setWindSpeed(1.3);
        weatherDto.setAtmosphericPressure(10d);
        weatherDto.setHumidity(73.2d);
        weatherDto.setWeatherConditions("Sunny");
        weatherDto.setLocation("Minsk");
        weatherDto.setDateTime(LocalDateTime.now());
    }

    @Test
    public void saveWeatherTest() {
        try {
            Long id = this.weatherService.saveWeather(weatherDto);
            assertThat(id)
                    .isNotNull()
                    .isNotNegative();
        }
        catch (ServiceException e) {
            fail("Test failed with ServiceException: " + e.getMessage());
        }
    }

    @Test
    public void getCurrentWeatherByLocationTest() {
        try {
            Long id = this.weatherService.saveWeather(weatherDto);
            weatherDto.setId(id);
            Optional<WeatherDto> retrievedWeatherOptional = this.weatherService.getCurrentWeatherByLocation("Minsk");
            assertThat(retrievedWeatherOptional)
                    .isPresent()
                    .hasValue(weatherDto);
        }
        catch (ServiceException e) {
            fail("Test failed with ServiceException: " + e.getMessage());
        }
    }

    @Test
    public void getWeatherInDateRangeTest() {
        try {
            LocalDateTime to = weatherDto.getDateTime().plusDays(1);
            LocalDateTime from = LocalDateTime.of(LocalDate.now().getYear(), 12, 1, 0, 0);
            Long id = this.weatherService.saveWeather(weatherDto);
            weatherDto.setId(id);
            WeatherDto secondWeatherDto = new WeatherDto();
            secondWeatherDto.setTemperature(4.5d);
            secondWeatherDto.setWindSpeed(1.3);
            secondWeatherDto.setAtmosphericPressure(10d);
            secondWeatherDto.setHumidity(73.2d);
            secondWeatherDto.setWeatherConditions("Sunny");
            secondWeatherDto.setLocation("Minsk");
            secondWeatherDto.setDateTime(from.plusDays(5));
            Long secondId = this.weatherService.saveWeather(secondWeatherDto);
            secondWeatherDto.setId(secondId);

            List<WeatherDto> expected = new ArrayList<>();
            expected.add(weatherDto);
            expected.add(secondWeatherDto);
            List<WeatherDto> weatherDtoList = this.weatherService.getWeatherInDateRange(from, to);
            assertThat(weatherDtoList)
                    .isNotEmpty()
                    .hasSize(2)
                    .isEqualTo(expected);
        }
        catch (ServiceException e) {
            fail("Test failed with ServiceException: " + e.getMessage());
        }
    }

    @Test
    public void getAverageDailyTemperature() {
        try {
            LocalDateTime to = LocalDateTime.of(2023, 11, 6, 0, 0);
            LocalDateTime from = LocalDateTime.of(2023, 11, 2, 0, 0);

            Double expected = 7.4d;
            Double averageTemp = this.weatherService.getAverageDailyTemperature(from, to);
            assertThat(averageTemp)
                    .isEqualTo(expected);
        }
        catch (ServiceException e) {
            fail("Test failed with ServiceException: " + e.getMessage());
        }
    }
}
