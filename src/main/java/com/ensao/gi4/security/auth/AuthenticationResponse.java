package com.ensao.gi4.security.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String accessToken, String refreshToken) {
}
