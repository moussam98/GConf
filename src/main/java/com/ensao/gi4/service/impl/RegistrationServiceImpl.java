package com.ensao.gi4.service.impl;

import org.springframework.stereotype.Service;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.User;
import com.ensao.gi4.service.api.RegistrationService;
import com.ensao.gi4.service.api.UserService;
import com.ensao.gi4.utils.EmailValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService{
	
	private final UserService userService; 
	private final EmailValidator emailValidator; 

	@Override
	public String register(UserDto userDto) {
		
		boolean isValidEmail = emailValidator.test(userDto.getEmail());
		
		if (!isValidEmail) {
			return "-2"; 
		}
		
		User user  = Mapper.toUser(userDto);
		
		Long userId = userService.SignUp(user);
		return userId.toString();
	}

}
