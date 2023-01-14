package com.ensao.gi4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
	
	private String firstname; 
	private String lastname;
	private String email;
	private String password; 

}
