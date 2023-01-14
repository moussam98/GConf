package com.ensao.gi4.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "application.jwt")
@Configuration
@Getter
@Setter
public class JwtConfig {
	
	private String secretKey; 
	private String tokenPrefix; 
	private Integer tokenExpirationAfterDays; 

}
