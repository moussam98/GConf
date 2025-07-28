package com.ensao.gi4.security.token;

public interface TokenService {

    void revokeAllUserTokens(Long userId);
    Token save(Token token);
    boolean isRevoked(String token);
}
