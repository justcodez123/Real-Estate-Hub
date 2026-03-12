package com.real_estate.real_estate_backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
	/*For now, we will send just a fake token or UUID, later we will send JWT*/
	private String token;
	private Long id;
	private String firstName;
	private String email;
	private String role;
	private String subscriptionType;
	
	public AuthResponse(String token, Long id, String firstName, String email, String role, String subscriptionType) {
		this.token = token;
		this.id = id;
		this.firstName = firstName;
		this.email = email;
		this.role = role;
		this.subscriptionType = subscriptionType;
	}
	
}
