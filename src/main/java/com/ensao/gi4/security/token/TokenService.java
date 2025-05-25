package com.ensao.gi4.security.token;

import com.ensao.gi4.model.User;

public interface TokenService {

    void revokeAllUserTokens(User user);
    Token save(Token token);
    boolean isRevoked(String token);
}
