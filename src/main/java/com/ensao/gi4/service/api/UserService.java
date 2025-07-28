
package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.UserPatchDto;
import com.ensao.gi4.dto.UserRequestDto;
import com.ensao.gi4.dto.UserResponseDto;
import com.ensao.gi4.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
	UserResponseDto register(UserRequestDto userDto);
	Optional<UserResponseDto> findById(Long id);
	List<UserResponseDto> findAll();
	Optional<UserResponseDto> findByEmail(String email);
	Integer deleteByEmail(String email);
	UserResponseDto update(UserPatchDto userPatchDto, String email);
	List<User> saveAll(List<User> users);

}
