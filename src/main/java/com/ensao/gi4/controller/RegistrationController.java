package com.ensao.gi4.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.service.api.RegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration")
public class RegistrationController {
	
	private final RegistrationService registrationService;
	
	@PostMapping
	public String register(@RequestBody UserDto userDto) {
		return registrationService.register(userDto); 
	}

}
