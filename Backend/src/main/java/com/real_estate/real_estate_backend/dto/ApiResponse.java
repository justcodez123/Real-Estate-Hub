package com.real_estate.real_estate_backend.dto;

import java.util.List;

public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;

    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    // Getters and Setters needed here 
    public boolean isSuccess() { return success; }
    public Object getData() { return data; }
}