package com.ensao.gi4.controller;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.dto.UserPatchDto;
import com.ensao.gi4.dto.UserRequestDto;
import com.ensao.gi4.service.api.UserService;
import com.ensao.gi4.utils.MessageSourceUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
record UserController(UserService userService,
                      MessageSourceUtils messageSourceUtils) {

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> findUsers(@RequestParam(required = false) String email) {
        return (email != null) ? getUserByEmail(email) : getUsers();
    }

    private ResponseEntity<UserDto> getUserByEmail(String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("{email}")
    public ResponseEntity<String> deleteUserByEmail(@PathVariable String email) {
        if (userService.deleteByEmail(email) == 1) {
            return ResponseEntity.ok(messageSourceUtils
                    .getMessage("organizer.account.deleted", new Object[]{email}));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(messageSourceUtils.getMessage("organizer.account.not_found"));
    }

    @PatchMapping("{email}")
    public ResponseEntity<Object> updateUserByEmail(@PathVariable String email,
                                                    @Valid @RequestBody UserPatchDto userPatchDto) {
        return ResponseEntity.ok(userService.patchByEmail(email, userPatchDto));
    }

    @PostMapping
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(userService.create(userDto));
    }

}

