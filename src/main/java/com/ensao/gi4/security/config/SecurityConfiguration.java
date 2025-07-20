package com.ensao.gi4.security.config;

import com.ensao.gi4.security.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthorizationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                 .cors(Customizer.withDefaults())
                 .csrf(CsrfConfigurer::disable)
                 .sessionManagement(session ->
                         session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                         .requestMatchers("/api/v*/auth/**").permitAll()
                         .requestMatchers(HttpMethod.POST, "/api/v*/users").permitAll()
                         .requestMatchers(HttpMethod.POST, "/api/v*/users/*/submissions").permitAll()
                         .requestMatchers("/api/v*/users/*/conferences").hasAuthority("ADMIN")
                         .requestMatchers("/api/v*/users/**").hasAuthority("ADMIN")
                         .requestMatchers("/api/v*/conferences/*/cfp/**").hasAuthority("ADMIN")
                         .requestMatchers("/api/v*/conferences/**").hasAuthority("ADMIN")
                         .requestMatchers("/api/v*/submissions/**").hasAuthority("ADMIN")
                         .requestMatchers("/api/v*/documents/**").hasAuthority("ADMIN")
                         .anyRequest().authenticated()
                 )
                 .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
