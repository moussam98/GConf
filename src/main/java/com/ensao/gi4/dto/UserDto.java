package com.ensao.gi4.dto;

import com.ensao.gi4.model.Role;

import java.time.Instant;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        Instant createdAt,
        Instant updatedAt) {

}
