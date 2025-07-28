package com.ensao.gi4.dto;

import com.ensao.gi4.model.Role;

import java.time.Instant;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        Instant createdAt,
        Instant updatedAt,
        Long conferenceId) {

}
