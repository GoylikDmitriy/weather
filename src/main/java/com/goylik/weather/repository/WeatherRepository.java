package com.goylik.weather.repository;

import com.goylik.weather.model.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @Query("SELECT weather FROM Weather weather WHERE weather.dateTime BETWEEN :startDate AND :endDate")
    List<Weather> findWeatherInDateRange(@Param("startDate") LocalDateTime from, @Param("endDate") LocalDateTime to);
    Optional<Weather> findFirstByLocationOrderByDateTimeDesc(String location);
}
