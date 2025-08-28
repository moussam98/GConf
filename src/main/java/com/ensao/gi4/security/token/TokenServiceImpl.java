package com.ensao.gi4.security.token;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.dto.mapper.UserMapper;
import com.ensao.gi4.security.jwt.JwtService;
import com.ensao.gi4.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public String generateAccessToken(Map<String, Object> claims, String username) {
        return jwtService.generateAccessToken(claims, username);
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, String username) {
        return jwtService.generateRefreshToken(extraClaims ,username);
    }



    @Override
    public Map<String, Object> createUserClaims(String subject) {
        var user = userService.getByEmail(subject);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", List.of(user.role().name()));
        claims.put("userId", user.id());
        return claims;
    }

    public void saveUserTokens(String subject, String accessToken, String refreshToken) {
        var userDto = userService.getByEmail(subject);
        revokeAllUserTokens(userDto.id());
        saveToken(userDto, accessToken, TokenType.ACCESS);
        saveToken(userDto, refreshToken, TokenType.REFRESH);
    }

    private void saveToken(UserDto userDto, String jwtToken, TokenType tokenType) {
        var token = Token.builder()
                .user(userMapper.toUser(userDto))
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Long userId) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(userId);
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        return jwtService.isRefreshTokenValid(refreshToken) && this.isNotRevoked(refreshToken);
    }

    public String extractSubject(String token) {
        return jwtService.extractUsername(token);
    }

    private boolean isNotRevoked(String token) {
        return !tokenRepository.findByToken(token).map(Token::isRevoked).orElseThrow(()
                -> new InvalidTokenException("Invalid token"));
    }

    @Override
    public boolean isTokenValid(String token, String expectedSubject) {
        return jwtService.isTokenValid(token, expectedSubject) && this.isNotRevoked(token);
    }
}