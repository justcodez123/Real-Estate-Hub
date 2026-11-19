package com.real_estate.real_estate_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import java.util.Arrays;
import java.util.List;

/**
 * IMPORTANT: Spring Boot Doesnt recognize this as Security and CORS configuration,
 * so you have to add @Configuration and @EnableWebSecurity.
 * Other way is that you can directly import it in your main Application Class
 * in @main function by adding @import(SecurityConfig.class)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inject the list from application.properties
    // The syntax after the colon (:http://localhost:3000) acts as a fallback default if the property is missing
    @Value("${app.cors.allowed-origins:http://localhost:5173,http://localhost:3000}")
    private List<String> allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(allowedOrigins);

        // Pass the injected origins list here
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Access-Control-Allow-Origin"));
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // 1. Modern CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Modern CSRF disable syntax using method reference
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Modern request authorization
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
