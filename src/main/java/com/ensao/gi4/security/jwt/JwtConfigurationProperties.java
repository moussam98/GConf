package com.ensao.gi4.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "application.jwt")
@Configuration
@Getter
@Setter
public class JwtConfigurationProperties {
	
	private String secretKey; 
    private Long tokenExpirationInMilliseconds;
    private Long tokenRefreshExpirationInMilliseconds;

}
