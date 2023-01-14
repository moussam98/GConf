package com.ensao.gi4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.service.api.RegistrationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
	
	private final RegistrationService registrationService;
	
	@PostMapping
	public ResponseEntity<String> register(@RequestBody UserDto userDto) {
		
		String result = registrationService.register(userDto);
		if ("-1".equals(result)) {
			return ResponseEntity.badRequest().body("Email already taken");
		}else if ("-2".equals(result)) {
			return ResponseEntity.badRequest().body("Email invalid"); 
		} else {
			return ResponseEntity.ok().body(result);  
		}
		
	}

}
