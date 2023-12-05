package com.goylik.weather.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DateRangeRequest {
    @NotBlank(message = "From date is required.")
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Invalid date format. Should be dd-MM-yyyy")
    private String from;
    @NotBlank(message = "To date is required.")
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Invalid date format. Should be dd-MM-yyyy")
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
