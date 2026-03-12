package com.real_estate.real_estate_backend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.real_estate.real_estate_backend.dto.RegisterRequest;
import com.real_estate.real_estate_backend.models.Users;
import com.real_estate.real_estate_backend.controller.enums.UserRole;
import com.real_estate.real_estate_backend.dto.ApiResponse;
import com.real_estate.real_estate_backend.dto.AuthResponse;
import com.real_estate.real_estate_backend.dto.LoginRequest;
import com.real_estate.real_estate_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	
	//--REGISTRATIONS-- For Both Users and Agents//
	
	@PostMapping("/register")
	public ApiResponse register(@RequestBody RegisterRequest request) {
			if(userRepository.existsByEmail(request.getEmail())) {
				return new ApiResponse(false, String.format("Email with %s already exists", request.getEmail()), null);
			}
			
			Users user = new Users();
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setEmail(request.getEmail());
			user.setPassword(request.getPassword());
			user.setPhone(request.getPhone());
			
			//Default to USER if no role provided
			String roleStr = (request.getRole() != null)? request.getRole().toUpperCase() : "USER";
			
			try {
				user.setRole(UserRole.valueOf(roleStr));
			} catch (IllegalArgumentException e) {
				user.setRole(UserRole.USER);
				e.printStackTrace();
			}
			
			userRepository.save(user);
			return new ApiResponse(true, "User Registration Successful", null);
		}
		
		/*LOGIN - FOR USER ONLY*/
		
		@PostMapping("/login")
		public ApiResponse login(@RequestBody LoginRequest request) {
			Users user = userRepository.findByEmail(request.getEmail()).orElse(null);
			
			//User Exists And Password Matches
			if(user == null || !user.getPassword().equals(request.getPassword())) {
				return new ApiResponse(false, "Invalid Email or Password", null);
			}
			
			//Frontend is expecting a token, so currently we are sending the UUID, Later change it to JWT
			String token = UUID.randomUUID().toString();
			
			AuthResponse authResponse = new AuthResponse(token, user.getId(), user.getFirstName(), user.getEmail(),user.getRole().toString(), user.getSubscriptionType());
			
			return new ApiResponse(true, "Login Succesful", authResponse);
		}
		
		/*LOGIN - FOR AGENT ONLY*/
		
		@PostMapping("/agent-login")
		public ApiResponse agentLogin(@RequestBody LoginRequest request) {
			/*We will use login method here basically*/
			Users user = userRepository.findByEmail(request.getEmail()).orElse(null);
			
			if(user == null) {
				return new ApiResponse(false, "Invalid email or Password", null);
			}
			
			if(user.getRole() != UserRole.AGENT) {
				return new ApiResponse(false, "Not an Agent", null);
			}
			return login(request);
		}
	
}
