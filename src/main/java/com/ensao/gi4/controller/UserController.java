package com.ensao.gi4.controller;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.model.User;
import com.ensao.gi4.service.api.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
	
	private final UserService userService; 
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
		Optional<User> user = userService.findById(userId);
        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.noContent().build());
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers(){
		Optional<List<User>> usersOptional = userService.findAll();
        return usersOptional
				.map(users -> ResponseEntity.ok().body(users))
				.orElseGet(() -> ResponseEntity.noContent().build());
	}
	
	@GetMapping("/username/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		Optional<User> personOptional = userService.findByEmail(email);
        return personOptional
				.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}
	
	@DeleteMapping("/delete/{email}")
	public ResponseEntity<String> deleteUserByEmail(@PathVariable String email){
		Integer result = userService.deleteByEmail(email); 
		if (result == 1) {
			return new ResponseEntity<>("Person deleted !", HttpStatus.OK);
		}
		return new ResponseEntity<>("Person not found !", HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/update/{email}")
	public ResponseEntity<String> updateUserByEmail(@RequestBody UserDto userDto, @PathVariable("email") String email){
		Boolean isUpdate = userService.update(userDto, email); 
		
		if (isUpdate) {
			return ResponseEntity.ok().body("User updated successfully");
		}else {
			return ResponseEntity.badRequest().body("User not found !");
		}
		
	}


}
