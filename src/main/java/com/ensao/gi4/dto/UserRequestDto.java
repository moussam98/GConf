package com.ensao.gi4.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @NotBlank(message = "{error.user.firstname.notBlank}") String firstName,
        @NotBlank(message = "{error.user.lastname.notBlank}") String lastName,
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "{error.user.email.invalid}") String email,
        @Size(min = 8, message = "{error.user.password.size.regexp}") String password
) {

}
