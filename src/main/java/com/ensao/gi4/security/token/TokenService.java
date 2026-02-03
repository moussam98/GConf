package com.ensao.gi4.security.token;

public interface TokenService {
    // Token Generation
    String  generateAccessToken(String username);
    String generateRefreshToken(String username);

    // Token Management
    void saveUserTokens(String subject, String accessToken, String refreshToken);

    // Token validation
    boolean isRefreshTokenValid(String refreshToken);
    boolean isTokenValid(String token, String expectedSubject);

    // Token Information Extraction
    String extractSubject(String token);
}
