package com.ensao.gi4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
	
	private final String firstname; 
	private final String lastname;
	private final String email;
	private final String password; 

}
