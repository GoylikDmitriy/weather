package com.goylik.weather.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather")
public class Weather extends BaseEntity {
    @Column(name = "temperature")
    private Double temperature;
    @Column(name = "wind_speed")
    private Double windSpeed;
    @Column(name = "atmospheric_pressure")
    private Double atmosphericPressure;
    @Column(name = "humidity")
    private Double humidity;
    @Column(name = "weather_conditions")
    private String weatherConditions;
    @Column(name = "location")
    private String location;
    @Column(name = "date_time")
    private LocalDateTime dateTime;

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
}
