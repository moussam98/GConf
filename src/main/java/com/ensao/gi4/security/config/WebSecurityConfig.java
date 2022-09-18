package com.ensao.gi4.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userService)
				.passwordEncoder(bCryptPasswordEncoder).and().build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager)
			throws Exception {

		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
		customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
		
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/api/v*/registration/**").permitAll()
		.antMatchers("/api/v*/login/**").permitAll()
		.antMatchers("/api/v*/user/**").hasAuthority("ADMIN")
		.antMatchers("/api/v*/conference/**").hasAuthority("ADMIN")
		.antMatchers("/api/v*/cfp/**").hasAuthority("ADMIN")
		.antMatchers("/api/v*/submission/**").hasAuthority("ADMIN")
		.antMatchers("/api/v*/document/**").hasAuthority("ADMIN")
		.anyRequest().authenticated()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilter(customAuthenticationFilter)
		.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
