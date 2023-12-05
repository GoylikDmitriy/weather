package com.goylik.weather.model.mapper;

import com.goylik.weather.model.dto.WeatherDto;
import com.goylik.weather.model.entity.Weather;
import com.goylik.weather.model.mapper.exception.JsonMappingException;

public interface WeatherMapper extends Mapper<Weather, WeatherDto> {
    WeatherDto mapToDtoFromJSON(String weatherJSON) throws JsonMappingException;
}
