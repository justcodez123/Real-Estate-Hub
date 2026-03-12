package com.real_estate.real_estate_backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
	private String email;
	private String password;
}
