package com.goylik.weather.model.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.model.entity.Weather;
import com.goylik.weather.model.mapper.WeatherMapper;
import com.goylik.weather.model.mapper.exception.JsonMappingException;
import com.goylik.weather.service.impl.WeatherServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Mapper class that maps between WeatherDto and Weather entities,
 * as well as JSON strings representing weather data.
 */
@Component
public class WeatherMapperImpl implements WeatherMapper {
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Override
    public WeatherDto mapToDtoFromJSON(String weatherJSON) {
        logger.info("Mapping JSON weather to Dto");
        if (weatherJSON == null) {
            logger.warn("JSON string is null.");
            return null;
        }

        try {
            String currentNode = "current";
            String locationNode = "location";

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(weatherJSON);
            Double temperature = jsonNode.get(currentNode).get("temp_c").asDouble();
            Double windSpeed = jsonNode.get(currentNode).get("wind_mph").asDouble();
            Double atmosphericPressure = jsonNode.get(currentNode).get("pressure_mb").asDouble();
            Double humidity = jsonNode.get(currentNode).get("humidity").asDouble();
            String weatherConditions = jsonNode.get(currentNode).get("condition").get("text").asText();
            String location = jsonNode.get(locationNode).get("name").asText();
            String dateString = jsonNode.get(currentNode).get("last_updated").asText();
            LocalDate date = this.parseDateFromJson(dateString);

            WeatherDto weatherDto = new WeatherDto();
            weatherDto.setTemperature(temperature);
            weatherDto.setWindSpeed(windSpeed);
            weatherDto.setAtmosphericPressure(atmosphericPressure);
            weatherDto.setHumidity(humidity);
            weatherDto.setWeatherConditions(weatherConditions);
            weatherDto.setLocation(location);
            weatherDto.setLastUpdated(date);

            logger.info("Weather JSON was mapped.");
            return weatherDto;
        } catch (JsonProcessingException e) {
            logger.error("Can't map JSON. Error: {}", e.getMessage());
            throw new JsonMappingException("Can't map JSON: " + e.getMessage());
        }
    }

    private LocalDate parseDateFromJson(String dateTimeString) {
        logger.info("Parsing date time. DateTime string: {}", dateTimeString);
        int year = Integer.parseInt(dateTimeString.substring(0, 4));
        int month = Integer.parseInt(dateTimeString.substring(5, 7));
        int day = Integer.parseInt(dateTimeString.substring(8, 10));
        LocalDate date = LocalDate.of(year, month, day);
        logger.info("Date was parsed.");
        return date;
    }

    /**
     * Maps a Weather entity to a WeatherDto object.
     *
     * @param weather the Weather entity to be mapped.
     * @return a WeatherDto object mapped from the Weather entity.
     */
    @Override
    public WeatherDto map(Weather weather) {
        logger.info("Mapping weather entity to dto.");
        if (weather == null) {
            logger.warn("Weather entity is null.");
            return null;
        }

        WeatherDto weatherDto = new WeatherDto();
        weatherDto.setId(weather.getId());
        weatherDto.setTemperature(weather.getTemperature());
        weatherDto.setWindSpeed(weather.getWindSpeed());
        weatherDto.setAtmosphericPressure(weather.getAtmosphericPressure());
        weatherDto.setHumidity(weather.getHumidity());
        weatherDto.setWeatherConditions(weather.getWeatherConditions());
        weatherDto.setLocation(weather.getLocation());
        weatherDto.setLastUpdated(weather.getDate());
        logger.info("Weather entity was mapped to dto.");
        return weatherDto;
    }

    /**
     * Maps a WeatherDto object to a Weather entity.
     *
     * @param weatherDto the WeatherDto object to be mapped.
     * @return a Weather entity mapped from the WeatherDto object.
     */
    @Override
    public Weather map(WeatherDto weatherDto) {
        logger.info("Mapping weather dto to entity.");
        if (weatherDto == null) {
            logger.warn("Weather dto is null");
            return null;
        }

        Weather weather = new Weather();
        weather.setId(weatherDto.getId());
        weather.setTemperature(weatherDto.getTemperature());
        weather.setWindSpeed(weatherDto.getWindSpeed());
        weather.setAtmosphericPressure(weatherDto.getAtmosphericPressure());
        weather.setHumidity(weatherDto.getHumidity());
        weather.setWeatherConditions(weatherDto.getWeatherConditions());
        weather.setLocation(weatherDto.getLocation());
        weather.setDate(weatherDto.getLastUpdated());
        logger.info("Weather dto was mapped.");
        return weather;
    }
}
