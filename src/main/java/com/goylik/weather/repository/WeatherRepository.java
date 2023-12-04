package com.goylik.weather.repository;

import com.goylik.weather.model.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @Query("SELECT weather FROM Weather weather WHERE weather.dateTime BETWEEN :from AND :to")
    List<Weather> findWeatherInDateRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
    Weather findWeatherByLocationAndDateTime(String location, LocalDateTime dateTime);
    Weather findWeatherByLocation(String location);
    Weather findWeatherById(Long id);
}
