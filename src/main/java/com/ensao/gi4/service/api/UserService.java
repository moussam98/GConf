
package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.dto.UserPatchDto;
import com.ensao.gi4.dto.UserRequestDto;
import com.ensao.gi4.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
	UserDto register(UserRequestDto userDto);
	Optional<UserDto> findById(Long id);
	List<UserDto> findAll();
	Optional<UserDto> findByEmail(String email);
	Integer deleteByEmail(String email);
	UserDto update(UserPatchDto userPatchDto, String email);
	List<User> saveAll(List<User> users);

}
