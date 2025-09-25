package com.ensao.gi4.service.impl;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.dto.UserPatchDto;
import com.ensao.gi4.dto.UserRequestDto;
import com.ensao.gi4.dto.mapper.UserMapper;
import com.ensao.gi4.model.Role;
import com.ensao.gi4.model.User;
import com.ensao.gi4.repository.UserRepository;
import com.ensao.gi4.service.api.UserService;
import com.ensao.gi4.service.exception.UserNotFoundException;
import com.ensao.gi4.service.exception.UsernameAlreadyExistsException;
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

public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessageSourceUtils messageSourceUtils;
	private final UserMapper userMapper;


	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
						   MessageSourceUtils messageSourceUtils, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.messageSourceUtils = messageSourceUtils;
		this.userMapper = userMapper;
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
	public UserDto create(UserRequestDto userRequestDto) {
		if (userRepository.findByEmail(userRequestDto.email()).isPresent()){
			throw new UsernameAlreadyExistsException(messageSourceUtils.getMessage("error.user.email.duplication",
					new Object[]{userRequestDto.email()}));
		}

		User user = new User();
		user.setFirstName(userRequestDto.firstName());
		user.setLastName(userRequestDto.lastName());
		user.setEmail(userRequestDto.email());
		user.setPassword(passwordEncoder.encode(userRequestDto.password()));
		user.setRole(Role.ADMIN);
		user.setCreatedAt(Instant.now());
		user.setUpdatedAt(Instant.now());
		return userMapper.toUserDto(userRepository.save(user));
	}

	@Override
	public Optional<UserDto> findById(Long id) {
		return userRepository.findById(id).map(userMapper::toUserDto);
	}

	@Override
	public UserDto getById(Long id) {
		return findById(id).orElseThrow(
				() -> new UserNotFoundException(messageSourceUtils.getMessage("organizer.account.not_found")));
	}

	@Override
	public List<UserDto> findAll() {
		return userRepository.findAll()
				.stream().map(userMapper::toUserDto).toList();
	}

	@Override
	public Optional<UserDto> findByEmail(String email) {
		return userRepository.findByEmail(email).map(userMapper::toUserDto);
	}

	@Override
	public UserDto getByEmail(String email) {
		return userRepository.findByEmail(email)
				.map(userMapper::toUserDto)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	}

	@Override
	@Transactional
	public Integer deleteByEmail(String email) {
		return userRepository.deleteByEmail(email);
	}

	@Override
	public UserDto patchByEmail(String email, UserPatchDto userPatchDto) {
		User user = userRepository.findByEmail(email).orElseThrow(() ->
				new UserNotFoundException(messageSourceUtils.getMessage("error.user.not_found", new Object[]{email})));

		updateUserFields(user, userPatchDto);
		return userMapper.toUserDto(userRepository.save(user));
	}

	private void updateUserFields(User target,UserPatchDto userPatchDto) {
		updateField(userPatchDto.firstName(), target::setFirstName);
		updateField(userPatchDto.lastName(), target::setLastName);
		updateEmailIfValid(userPatchDto.email(), target);
		target.setUpdatedAt(Instant.now());
		if (userPatchDto.password() != null && !userPatchDto.password().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(userPatchDto.password());
			target.setPassword(encodedPassword);
		}
	}

	private <T> void updateField(T fieldValue, Consumer<T> fieldSetter){
		if (fieldValue != null) {
			fieldSetter.accept(fieldValue);
		}
	}

	private void updateEmailIfValid(String newEmail, User user) {
		if(newEmail != null && (newEmail.equals(user.getEmail()) || userRepository.existsByEmail(user.getEmail()))) {
			throw new IllegalStateException(messageSourceUtils.getMessage("error.user.email.duplication",
					new Object[]{newEmail}));
		}
	}


}
