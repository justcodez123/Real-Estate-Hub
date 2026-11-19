package com.real_estate.real_estate_backend.dto;

import lombok.Getter;

import java.util.List;

public class ApiResponse {
    // Getters and Setters needed here
    @Getter
    private final boolean success;
    private String message;
    @Getter
    private final Object data;

    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}