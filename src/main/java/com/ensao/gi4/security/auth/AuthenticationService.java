package com.ensao.gi4.security.auth;

import com.ensao.gi4.model.User;
import com.ensao.gi4.security.jwt.JwtService;
import com.ensao.gi4.security.token.InvalidTokenException;
import com.ensao.gi4.security.token.Token;
import com.ensao.gi4.security.token.TokenService;
import com.ensao.gi4.security.token.TokenType;
import com.ensao.gi4.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        User user = fetchUserByEmail(request.email());
        Map<String, Object> claims = getUserClaims(user);
        var jwtToken = jwtService.generateAccessToken(claims, user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserTokens(user, jwtToken, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private User fetchUserByEmail(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private static Map<String, Object> getUserClaims(User user) {
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        return claims;
    }

    private void saveUserToken(User user, String jwtToken, TokenType tokenType) {
        var token = Token.builder()
                .user(user)
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

            if (jwtService.isRefreshTokenValid(request.refreshToken(), user)) {
                var accessToken = jwtService.generateAccessToken(getUserClaims(user), user);
                var refreshTokenRotation = jwtService.generateRefreshToken(user);
                saveUserTokens(user, accessToken, refreshTokenRotation);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshTokenRotation)
                        .build();
            }
        }
        throw new InvalidTokenException("Invalid refresh token");
    }

    private void saveUserTokens(User user, String accessToken, String refreshToken) {
        tokenService.revokeAllUserTokens(user);
        saveUserToken(user, accessToken, TokenType.ACCESS);
        saveUserToken(user, refreshToken, TokenType.REFRESH);
    }


}