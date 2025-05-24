package com.ensao.gi4.service.impl;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.model.Role;
import com.ensao.gi4.model.User;
import com.ensao.gi4.repository.UserRepository;
import com.ensao.gi4.service.api.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<User> userOptional = userRepository.findByEmail(email);
		
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					user.getAuthorities());
		} else {
			throw new UsernameNotFoundException(String.format("User with email %s not found", email));
		}

	}

	@Override
	public Long SignUp(User user) {
		boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

		if (userExists) {
			return -1L;
		}

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setRole(Role.ADMIN);

		return userRepository.save(user).getId();
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<List<User>> findAll() {
		return Optional.of(userRepository.findAll());
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Integer deleteByEmail(String email) {
		return userRepository.deleteByEmail(email);
	}

	@Override
	public Boolean update(UserDto userDto, String email) {

		Optional<User> optionalUser = userRepository.findByEmail(email);

		if (optionalUser.isPresent()) {

			User user = optionalUser.get();
			user.setFirstname(userDto.getFirstname());
			user.setLastname(userDto.getLastname());
			user.setEmail(userDto.getEmail());

			if ( userDto.getPassword() != null && !userDto.getPassword().isEmpty()){
				String encodedPassword = passwordEncoder.encode(userDto.getPassword());
				user.setPassword(encodedPassword);
			}

			return true;
		}

		return false;
	}

}
