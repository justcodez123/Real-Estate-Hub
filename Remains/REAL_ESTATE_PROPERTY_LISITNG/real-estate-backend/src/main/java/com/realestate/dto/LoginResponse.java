package com.realestate.dto;

import com.realestate.model.Role;
import com.realestate.model.SubscriptionType;
import com.realestate.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserType userType;
    private Role role;
    private SubscriptionType subscriptionType;
    private boolean active;
}
