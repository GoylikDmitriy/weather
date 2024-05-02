package com.goylik.weather.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDto {
    private Long id;
    @JsonProperty("temp")
    private Double temperature;
    @JsonProperty("wind_mph")
    private Double windSpeed;
    @JsonProperty("pressure_mb")
    private Double atmosphericPressure;
    @JsonProperty("humidity")
    private Double humidity;
    @JsonProperty("weather_conditions")
    private String weatherConditions;
    @JsonProperty("location")
    private String location;
    @JsonProperty("last_updated")
    private LocalDate lastUpdated;
}
