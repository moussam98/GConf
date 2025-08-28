package com.ensao.gi4.security.auth;

import com.ensao.gi4.security.token.InvalidTokenException;
import com.ensao.gi4.security.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticateUser(request.email(), request.password());
        String username = authentication.getName();
        Map<String, Object> claims = extractClaims(authentication);
        String accessToken = tokenService.generateAccessToken(claims, username);
        String refreshToken = tokenService.generateRefreshToken(claims, username);
        tokenService.saveUserTokens(username, accessToken, refreshToken);
        return buildAuthenticationResponse(accessToken, refreshToken);
    }

    private Authentication authenticateUser(String email, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        if (!tokenService.isRefreshTokenValid(request.refreshToken())) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String username = tokenService.extractSubject(request.refreshToken());
        Map<String, Object> claims = tokenService.createUserClaims(username);
        String newAccessToken = tokenService.generateAccessToken(claims, username);
        String newRefreshToken = tokenService.generateRefreshToken(claims, username);
        tokenService.saveUserTokens(username, newAccessToken, newRefreshToken);
        return buildAuthenticationResponse(newAccessToken, newRefreshToken);
    }

    private Map<String, Object> extractClaims(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", authorities);
        claims.put("authenticated", authentication.isAuthenticated());
        return claims;
    }

    private AuthenticationResponse buildAuthenticationResponse(String accessToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}