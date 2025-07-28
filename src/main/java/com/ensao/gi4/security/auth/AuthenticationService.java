package com.ensao.gi4.security.auth;

import com.ensao.gi4.dto.UserResponseDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.Role;
import com.ensao.gi4.security.jwt.JwtService;
import com.ensao.gi4.security.token.InvalidTokenException;
import com.ensao.gi4.security.token.Token;
import com.ensao.gi4.security.token.TokenService;
import com.ensao.gi4.security.token.TokenType;
import com.ensao.gi4.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(authenticationToken);
        UserResponseDto userResponseDto = fetchUserByEmail(request.email());
        Map<String, Object> claims = getUserClaims(userResponseDto.role());
        var jwtToken = jwtService.generateAccessToken(claims, userResponseDto.email());
        var refreshToken = jwtService.generateRefreshToken(userResponseDto.email());
        saveUserTokens(userResponseDto, jwtToken, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UserResponseDto fetchUserByEmail(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private static Map<String, Object> getUserClaims(Role role) {
        List<Role> roles = Collections.singletonList(role);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        return claims;
    }

    private void saveUserToken(UserResponseDto userResponseDto, String jwtToken, TokenType tokenType) {
        var token = Token.builder()
                .user(Mapper.toUser(userResponseDto))
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(token);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String userEmail = jwtService.extractUsername(request.refreshToken());

        if (userEmail != null) {
            var user = fetchUserByEmail(userEmail);
            if (jwtService.isRefreshTokenValid(request.refreshToken(), user.email())) {
                var accessToken = jwtService.generateAccessToken(getUserClaims(user.role()), user.email());
                var refreshTokenRotation = jwtService.generateRefreshToken(user.email());
                saveUserTokens(user, accessToken, refreshTokenRotation);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshTokenRotation)
                        .build();
            }
        }
        throw new InvalidTokenException("Invalid refresh token");
    }

    private void saveUserTokens(UserResponseDto userResponseDto, String accessToken, String refreshToken) {
        tokenService.revokeAllUserTokens(userResponseDto.id());
        saveUserToken(userResponseDto, accessToken, TokenType.ACCESS);
        saveUserToken(userResponseDto, refreshToken, TokenType.REFRESH);
    }


}