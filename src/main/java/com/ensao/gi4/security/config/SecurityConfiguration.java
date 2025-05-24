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
                         .requestMatchers("/api/v*/registration/**").permitAll()
                         .requestMatchers("/api/v*/login/**").permitAll()
                         .requestMatchers("/api/v*/user/**").hasAuthority("ADMIN")
                         .requestMatchers("/api/v*/conference/**").hasAuthority("ADMIN")
                         .requestMatchers("/api/v*/cfp/**").hasAuthority("ADMIN")
                         .requestMatchers(HttpMethod.POST, "/api/v1/submission/add/**").permitAll()
                         .requestMatchers("/api/v*/submission/**").hasAuthority("ADMIN")
                         .requestMatchers("/api/v*/document/**").hasAuthority("ADMIN")
                         .requestMatchers("/api/v*/auth/authenticate").permitAll()
                         .anyRequest().authenticated()
                 )
                 .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
