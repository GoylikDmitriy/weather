package com.goylik.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goylik.weather.model.dto.DateRangeRequest;
import com.goylik.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
@AutoConfigureMockMvc
public class WeatherControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void testWeatherCurrentEndpoint() throws Exception {
        mvc.perform(get("/weather/current")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAverageDailyTemperatureEndpoint() throws Exception {
        DateRangeRequest request = new DateRangeRequest();
        request.setFrom("02-11-2023");
        request.setTo("06-11-2023");
        Double averageTemperature = 7.4d;
        when(weatherService.getAverageDailyTemperature(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(averageTemperature);

        mvc.perform(post("/weather/average-daily-temperature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.average_temp", equalTo(averageTemperature)));
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
