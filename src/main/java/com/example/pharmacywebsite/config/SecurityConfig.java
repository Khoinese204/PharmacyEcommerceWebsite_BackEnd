package com.example.pharmacywebsite.config;

import com.example.pharmacywebsite.service.JwtAuthenticationFilter;
import com.example.pharmacywebsite.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html") // Mở swagger bằng cách ấn vào link =>
                                            // http://localhost:8080/swagger-ui/index.html#/
                .permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/cart/**").permitAll()
                .requestMatchers("/api/categories/**").permitAll()
                .requestMatchers("/api/medicines/**").permitAll()
                .requestMatchers("/api/orders/**").permitAll()
                .requestMatchers("/api/payments/**").permitAll()
                .requestMatchers("/api/v1/payment/**").permitAll()
                .requestMatchers("/api/shipments/**").permitAll()
                .requestMatchers("/api/suppliers/**").permitAll()
                .requestMatchers("/api/import/**").permitAll()
                .requestMatchers("/api/inventory/**").permitAll()
                .requestMatchers("/api/orders/**").permitAll()
                .requestMatchers("/api/exports/**").permitAll()
                .requestMatchers("/api/promotions/**").permitAll()
                .requestMatchers("/api/pricing/**").permitAll()
                .requestMatchers("/api/upload/**").permitAll()
                .requestMatchers("/api/warehouse/**").permitAll()
                .requestMatchers("/images/**").permitAll() // ✅ mở quyền truy cập ảnh
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/admin/dashboard/**").permitAll()
                .requestMatchers("/images/**", "/images/avatar/**").permitAll()
                .requestMatchers("/uploads/categories/**").permitAll()
                .requestMatchers("/ws/**").permitAll() // mở quyền truy cập WebSocket endpoint
                .anyRequest().authenticated();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
