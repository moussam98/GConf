package com.ensao.gi4.security.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ensao.gi4.security.filter.CustomAuthenticationFilter;
import com.ensao.gi4.security.filter.CustomAuthorizationFilter;
import com.ensao.gi4.service.api.UserService;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
			UserService userService) throws Exception {
		AuthenticationManagerBuilder authenticationBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		return authenticationBuilder.userDetailsService(userService)
		.passwordEncoder(bCryptPasswordEncoder).and().build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, JwtConfig jwtConfig)
			throws Exception {

		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager, jwtConfig);
		customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
		
		http.cors()
			.and()
			.csrf().disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilter(customAuthenticationFilter)
			.addFilterAfter(new CustomAuthorizationFilter(jwtConfig), CustomAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers("/api/v*/registration/**").permitAll()
			.antMatchers("/api/v*/login/**").permitAll()
			.antMatchers("/api/v*/user/**").hasAuthority("ADMIN")
			.antMatchers("/api/v*/conference/**").hasAuthority("ADMIN")
			.antMatchers("/api/v*/cfp/**").hasAuthority("ADMIN")
			.antMatchers(HttpMethod.POST, "/api/v1/submission/add/**").anonymous()
			.antMatchers("/api/v*/submission/**").hasAuthority("ADMIN")
			.antMatchers("/api/v*/document/**").hasAuthority("ADMIN")
			.anyRequest().authenticated(); 

		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source; 
	}

}
