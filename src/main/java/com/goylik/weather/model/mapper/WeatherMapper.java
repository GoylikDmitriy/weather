package com.goylik.weather.model.mapper;

import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.model.entity.Weather;
import com.goylik.weather.model.mapper.exception.JsonMappingException;

/**
 * Interface for mapping between Weather entity and WeatherDto objects, as well as mapping from JSON to WeatherDto.
 */
public interface WeatherMapper extends Mapper<Weather, WeatherDto> {
    /**
     * Maps a JSON string representing weather data to a WeatherDto object.
     *
     * @param weatherJSON the JSON string representing weather data.
     * @return a WeatherDto object mapped from the JSON string.
     * @throws JsonMappingException if there are issues with mapping the JSON string.
     */
    WeatherDto mapToDtoFromJSON(String weatherJSON) throws JsonMappingException;
}
