package com.goylik.weather.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.model.entity.Weather;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WeatherMapper implements Mapper<Weather, WeatherDto> {
    public WeatherDto mapToDtoFromJSON(String weatherJSON) {
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
            LocalDateTime dateTime = this.parseDateTime(dateTimeString);

            WeatherDto weatherDto = new WeatherDto();
            weatherDto.setTemperature(temperature);
            weatherDto.setWindSpeed(windSpeed);
            weatherDto.setAtmosphericPressure(atmosphericPressure);
            weatherDto.setHumidity(humidity);
            weatherDto.setWeatherConditions(weatherConditions);
            weatherDto.setLocation(location);
            weatherDto.setDateTime(dateTime);

            return weatherDto;
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Can't parse JSON: " + e.getMessage());
        }
    }


    private LocalDateTime parseDateTime(String dateTimeString) {
        int year = Integer.parseInt(dateTimeString.substring(0, 4));
        int month = Integer.parseInt(dateTimeString.substring(5, 7));
        int day = Integer.parseInt(dateTimeString.substring(8, 10));
        int hour = Integer.parseInt(dateTimeString.substring(11, 13));
        int minute = Integer.parseInt(dateTimeString.substring(14, 16));
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    @Override
    public WeatherDto map(Weather weather) {
        WeatherDto weatherDto = new WeatherDto();
        weatherDto.setId(weather.getId());
        weatherDto.setTemperature(weather.getTemperature());
        weatherDto.setWindSpeed(weather.getWindSpeed());
        weatherDto.setAtmosphericPressure(weather.getAtmosphericPressure());
        weatherDto.setHumidity(weather.getHumidity());
        weatherDto.setWeatherConditions(weather.getWeatherConditions());
        weatherDto.setLocation(weather.getLocation());
        weatherDto.setDateTime(weather.getDateTime());
        return weatherDto;
    }

    @Override
    public Weather map(WeatherDto weatherDto) {
        Weather weather = new Weather();
        weather.setId(weatherDto.getId());
        weather.setTemperature(weatherDto.getTemperature());
        weather.setWindSpeed(weatherDto.getWindSpeed());
        weather.setAtmosphericPressure(weatherDto.getAtmosphericPressure());
        weather.setHumidity(weatherDto.getHumidity());
        weather.setWeatherConditions(weatherDto.getWeatherConditions());
        weather.setLocation(weatherDto.getLocation());
        weather.setDateTime(weatherDto.getDateTime());
        return weather;
    }
}
