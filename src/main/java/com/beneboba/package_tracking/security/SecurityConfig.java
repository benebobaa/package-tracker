package com.beneboba.package_tracking.security;

import com.beneboba.package_tracking.entity.UserRole;
import com.beneboba.package_tracking.repository.UserRepository;
import com.beneboba.package_tracking.security.filter.JwtSecurityFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    private final JwtSecurityFilter jwtSecurityFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf( csrf -> csrf.disable() )
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/auth/**")
                                .permitAll()
                                .requestMatchers("/auth/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/services").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/locations").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/deliveries").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
