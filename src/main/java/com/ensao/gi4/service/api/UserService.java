package com.ensao.gi4.service.api;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.model.User;

public interface UserService extends UserDetailsService {
	Long SignUp(User user); 
	Optional<User> findById(Long id); 
	Optional<List<User>> findAll();
	Optional<User> findByEmail(String email);
	Integer deleteByEmail(String email);
	Boolean update(UserDto userDto, String email);

}
