package com.ensao.gi4.security.auth;

//TODO: Add a refresh token field and create an endpoint to obtain a new token when the current one expires
public record AuthenticationResponse(String token) {
}
