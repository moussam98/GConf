package com.ensao.gi4.security.token;

import java.util.Map;

public interface TokenService {
    // Token Generation
    String generateAccessToken(Map<String, Object> extraClaims, String username);
    String generateRefreshToken(Map<String, Object> extraClaims, String username);


    // Token Management
    void saveUserTokens(String subject, String accessToken, String refreshToken);

    // Token validation
    boolean isRefreshTokenValid(String refreshToken);
    boolean isTokenValid(String token, String expectedSubject);

    // Token Information Extraction
    String extractSubject(String token);
    // Claims Management
    Map<String, Object> createUserClaims(String subject);
}
