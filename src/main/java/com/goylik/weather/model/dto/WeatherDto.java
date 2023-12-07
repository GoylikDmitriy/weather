package com.goylik.weather.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private LocalDateTime dateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getAtmosphericPressure() {
        return atmosphericPressure;
    }

    public void setAtmosphericPressure(Double atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherDto that = (WeatherDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(temperature, that.temperature)
                && Objects.equals(windSpeed, that.windSpeed)
                && Objects.equals(atmosphericPressure, that.atmosphericPressure)
                && Objects.equals(humidity, that.humidity)
                && Objects.equals(weatherConditions, that.weatherConditions)
                && Objects.equals(location, that.location)
                && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, temperature, windSpeed, atmosphericPressure, humidity, weatherConditions, location, dateTime);
    }
}
