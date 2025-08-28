
package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.dto.UserPatchDto;
import com.ensao.gi4.dto.UserRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
	UserDto create(UserRequestDto userDto);
	Optional<UserDto> findById(Long id);
	UserDto getById(Long id);
	List<UserDto> findAll();
	Optional<UserDto> findByEmail(String email);
	UserDto getByEmail(String email);
	UserDto patchByEmail(String email, UserPatchDto userPatchDto);
	Integer deleteByEmail(String email);
}
