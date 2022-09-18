package com.ensao.gi4.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.model.User;
import com.ensao.gi4.service.api.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	@Autowired
	UserService userService; 
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
		return ResponseEntity.ok().body(userService.findById(userId).get());
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers(){
		Optional<List<User>> usersOptional = userService.findAll();
		
		if (usersOptional.isPresent()) {
			return ResponseEntity.ok().body(usersOptional.get());
		}else {
			return ResponseEntity.noContent().build(); 
		}
	}
	
	@GetMapping("/username/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		Optional<User> personOptional = userService.findByEmail(email);
		if (personOptional.isPresent()) {			
			return new ResponseEntity<User>(personOptional.get(), HttpStatus.OK); 
		}
		else {
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}
	}
	
	@DeleteMapping("/delete/{email}")
	public ResponseEntity<String> deleteUserByEmail(@PathVariable String email){
		Integer result = userService.deleteByEmail(email); 
		if (result == 1) {
			return new ResponseEntity<String>("Person deleted !", HttpStatus.OK); 
		}
		return new ResponseEntity<String>("Person not found !" ,HttpStatus.NOT_FOUND);
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
