package com.ensao.gi4.controller;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.model.User;
import com.ensao.gi4.service.api.RegistrationService;
import com.ensao.gi4.service.api.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
record UserController(UserService userService, RegistrationService registrationService) {

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> findUsers(@RequestParam(required = false) String email) {
        return (email != null) ? getUserByEmail(email) : getUsers();
    }

    private ResponseEntity<User> getUserByEmail(String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    private ResponseEntity<List<User>> getUsers() {
        return userService.findAll()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

    }

    @DeleteMapping("{email}")
    public ResponseEntity<String> deleteUserByEmail(@PathVariable String email) {
        if (userService.deleteByEmail(email) == 1) {
            return ResponseEntity.ok("User deleted !");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("{email}")
    public ResponseEntity<String> updateUserByEmail(@PathVariable String email, @RequestBody UserDto userDto) {
        if (userService.update(userDto, email)) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {

        String result = registrationService.register(userDto);
        if ("-1".equals(result)) {
            return ResponseEntity.badRequest().body("Email already taken");
        } else if ("-2".equals(result)) {
            return ResponseEntity.badRequest().body("Invalid email");
        } else {
            return ResponseEntity.ok().body(result);
        }
    }

}

