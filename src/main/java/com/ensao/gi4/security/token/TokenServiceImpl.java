package com.ensao.gi4.security.token;

import org.springframework.stereotype.Service;

@Service
public record TokenServiceImpl(TokenRepository tokenRepository) implements TokenService {

    @Override
    public void revokeAllUserTokens(Long userId) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(userId);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public boolean isRevoked(String token) {
        return tokenRepository.findByToken(token).map(Token::isRevoked).orElseThrow(()
                -> new InvalidTokenException("Invalid token"));
    }


}
