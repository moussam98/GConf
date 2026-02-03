package com.ensao.gi4.security.auth;

import com.ensao.gi4.security.token.InvalidTokenException;
import com.ensao.gi4.security.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticateUser(request.email(), request.password());
        String username = authentication.getName();
        String accessToken = tokenService.generateAccessToken(username);
        String refreshToken = tokenService.generateRefreshToken(username);
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
        String newAccessToken = tokenService.generateAccessToken(username);
        String newRefreshToken = tokenService.generateRefreshToken(username);
        tokenService.saveUserTokens(username, newAccessToken, newRefreshToken);
        return buildAuthenticationResponse(newAccessToken, newRefreshToken);
    }

    private AuthenticationResponse buildAuthenticationResponse(String accessToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}