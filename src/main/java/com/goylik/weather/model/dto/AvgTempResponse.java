package com.goylik.weather.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvgTempResponse {
    @JsonProperty("avg_temp")
    private Double averageTemp;
}
