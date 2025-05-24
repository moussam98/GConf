package com.ensao.gi4.security.auth;

import com.ensao.gi4.security.jwt.JwtService;
import com.ensao.gi4.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()
                ));
        var user = userService.findByEmail(request.email()).orElseThrow();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        var jwtToken = jwtService.generateToken(Map.of("roles", roles), user);
        return new AuthenticationResponse(jwtToken);
    }
}