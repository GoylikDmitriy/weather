package com.goylik.weather.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AverageTemperatureResponse {
    @JsonProperty("average_temp")
    private Double averageTemp;

    public Double getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(Double averageTemp) {
        this.averageTemp = averageTemp;
    }
}
