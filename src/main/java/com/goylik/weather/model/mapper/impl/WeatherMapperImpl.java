package com.goylik.weather.model.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.model.entity.Weather;
import com.goylik.weather.model.mapper.WeatherMapper;
import com.goylik.weather.model.mapper.exception.JsonMappingException;
import com.goylik.weather.model.mapper.exception.WeatherMappingException;
import com.goylik.weather.service.impl.WeatherServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper class that maps between WeatherDto and Weather entities,
 * as well as JSON strings representing weather data.
 */
@Component
public class WeatherMapperImpl implements WeatherMapper {
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Override
    public WeatherDto mapToDtoFromJSON(String weatherJSON) throws JsonMappingException {
        logger.info("Mapping JSON weather to Dto");
        if (weatherJSON == null || weatherJSON.isEmpty()) {
            logger.error("JSON string is null or empty");
            throw new JsonMappingException("JSON string is null or empty");
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
            String dateTimeString = jsonNode.get(currentNode).get("last_updated").asText();
            LocalDateTime dateTime = this.parseDateTimeFromJson(dateTimeString);

            WeatherDto weatherDto = new WeatherDto();
            weatherDto.setTemperature(temperature);
            weatherDto.setWindSpeed(windSpeed);
            weatherDto.setAtmosphericPressure(atmosphericPressure);
            weatherDto.setHumidity(humidity);
            weatherDto.setWeatherConditions(weatherConditions);
            weatherDto.setLocation(location);
            weatherDto.setDateTime(dateTime);

            logger.info("Weather JSON was mapped.");
            return weatherDto;
        }
        catch (JsonProcessingException e) {
            logger.error("Can't map JSON. Error: {}", e.getMessage());
            throw new JsonMappingException("Can't map JSON: " + e.getMessage());
        }
    }

    private LocalDateTime parseDateTimeFromJson(String dateTimeString) {
        logger.info("Parsing date time. DateTime string: {}", dateTimeString);
        int year = Integer.parseInt(dateTimeString.substring(0, 4));
        int month = Integer.parseInt(dateTimeString.substring(5, 7));
        int day = Integer.parseInt(dateTimeString.substring(8, 10));
        int hour = Integer.parseInt(dateTimeString.substring(11, 13));
        int minute = Integer.parseInt(dateTimeString.substring(14, 16));
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);
        logger.info("Date time was parsed.");
        return dateTime;
    }

    /**
     * Maps a Weather entity to a WeatherDto object.
     *
     * @param weather the Weather entity to be mapped.
     * @return a WeatherDto object mapped from the Weather entity.
     * @throws WeatherMappingException if there are issues with mapping the Weather entity.
     */
    @Override
    public WeatherDto map(Weather weather) throws WeatherMappingException {
        logger.info("Mapping weather entity to dto.");
        if (weather == null) {
            logger.error("Weather entity is null.");
            throw new WeatherMappingException("Weather entity is null.");
        }

        WeatherDto weatherDto = new WeatherDto();
        weatherDto.setId(weather.getId());
        weatherDto.setTemperature(weather.getTemperature());
        weatherDto.setWindSpeed(weather.getWindSpeed());
        weatherDto.setAtmosphericPressure(weather.getAtmosphericPressure());
        weatherDto.setHumidity(weather.getHumidity());
        weatherDto.setWeatherConditions(weather.getWeatherConditions());
        weatherDto.setLocation(weather.getLocation());
        weatherDto.setDateTime(weather.getDateTime());
        logger.info("Weather entity was mapped to dto.");
        return weatherDto;
    }

    /**
     * Maps a WeatherDto object to a Weather entity.
     *
     * @param weatherDto the WeatherDto object to be mapped.
     * @return a Weather entity mapped from the WeatherDto object.
     * @throws WeatherMappingException if there are issues with mapping the WeatherDto object.
     */
    @Override
    public Weather map(WeatherDto weatherDto) throws WeatherMappingException {
        logger.info("Mapping weather dto to entity.");
        if (weatherDto == null) {
            logger.error("Weather dto is null");
            throw new WeatherMappingException("Weather dto is null.");
        }

        Weather weather = new Weather();
        weather.setId(weatherDto.getId());
        weather.setTemperature(weatherDto.getTemperature());
        weather.setWindSpeed(weatherDto.getWindSpeed());
        weather.setAtmosphericPressure(weatherDto.getAtmosphericPressure());
        weather.setHumidity(weatherDto.getHumidity());
        weather.setWeatherConditions(weatherDto.getWeatherConditions());
        weather.setLocation(weatherDto.getLocation());
        weather.setDateTime(weatherDto.getDateTime());
        logger.info("Weather dto was mapped.");
        return weather;
    }
}
