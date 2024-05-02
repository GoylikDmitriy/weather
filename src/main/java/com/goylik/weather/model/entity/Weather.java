package com.goylik.weather.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "weather")
@Getter
@Setter
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
    @Column(name = "date")
    private LocalDate date;
}
