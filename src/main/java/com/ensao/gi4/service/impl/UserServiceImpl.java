package com.ensao.gi4.service.impl;

import com.ensao.gi4.dto.UserPatchDto;
import com.ensao.gi4.dto.UserRequestDto;
import com.ensao.gi4.dto.UserResponseDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.exception.UserNotFoundException;
import com.ensao.gi4.model.Role;
import com.ensao.gi4.model.User;
import com.ensao.gi4.repository.UserRepository;
import com.ensao.gi4.service.api.UserService;
import com.ensao.gi4.utils.MessageSourceUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessageSourceUtils messageSourceUtils;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
						   MessageSourceUtils messageSourceUtils) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.messageSourceUtils = messageSourceUtils;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
				.map(userDto -> new org.springframework.security.core.userdetails.User(userDto.getEmail(),
						userDto.getPassword(), userDto.getAuthorities()))
				.orElseThrow( () -> new UsernameNotFoundException(messageSourceUtils.getMessage(
						"participant.account.not_found", new Object[]{email})));
	}

	@Override
	public UserResponseDto register(UserRequestDto userRequestDto) {
		if (userRepository.findByEmail(userRequestDto.email()).isPresent()){
			throw new IllegalArgumentException(messageSourceUtils.getMessage("error.user.email.duplication",
					new Object[]{userRequestDto.email()}));
		}

		User user = Mapper.toUser(userRequestDto);
		user.setPassword(passwordEncoder.encode(userRequestDto.password()));
		user.setRole(Role.ADMIN);
		user.setCreatedAt(Instant.now());
		user.setUpdatedAt(Instant.now());
		return Mapper.toUserDto(userRepository.save(user));
	}

	@Override
	public Optional<UserResponseDto> findById(Long id) {
		return userRepository.findById(id).map(Mapper::toUserDto);
	}

	@Override
	public List<UserResponseDto> findAll() {
		return userRepository.findAll().stream().map(Mapper::toUserDto).toList();
	}

	@Override
	public Optional<UserResponseDto> findByEmail(String email) {
		return userRepository.findByEmail(email).map(Mapper::toUserDto);
	}

	@Override
	public Integer deleteByEmail(String email) {
		return userRepository.deleteByEmail(email);
	}

	@Override
	public UserResponseDto update(UserPatchDto userPatchDto, String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() ->
				new UserNotFoundException(messageSourceUtils.getMessage("error.user.not_found",
						new Object[]{email})));

		updateField(userPatchDto.firstName(), user::setFirstName);
		updateField(userPatchDto.lastName(), user::setLastName);
		updateEmailIfValid(userPatchDto.email(), user);
		user.setUpdatedAt(Instant.now());
		if (userPatchDto.password() != null && !userPatchDto.password().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(userPatchDto.password());
			user.setPassword(encodedPassword);
		}
		return Mapper.toUserDto(userRepository.save(user));
	}

	private void updateEmailIfValid(String newEmail, User user) {
		if(newEmail != null && (newEmail.equals(user.getEmail()) || userRepository.existsByEmail(user.getEmail()))) {
			throw new IllegalStateException(messageSourceUtils.getMessage("error.user.email.duplication",
					new Object[]{newEmail}));
		}
	}


	private <T> void updateField(T fieldValue, Consumer<T> fieldSetter){
		if (fieldValue != null) {
			fieldSetter.accept(fieldValue);
		}
	}

	@Override
	public List<User> saveAll(List<User> users) {
		for (var user: users){
			Instant instant = Instant.now();
			user.setCreatedAt(instant);
			user.setUpdatedAt(instant);
		}
		return userRepository.saveAll(users);
	}

}
