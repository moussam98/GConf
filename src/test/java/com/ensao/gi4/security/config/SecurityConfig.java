package com.ensao.gi4.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.ensao.gi4.model.Role;

@Configuration
public class SecurityConfig {
	
	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager(); 
		
		manager.createUser(
				User.withUsername("Ali")
					.password(passwordEncoder.encode("password"))
					.roles(Role.ADMIN.name()).build()
				);
		
		return manager; 
	}

}
