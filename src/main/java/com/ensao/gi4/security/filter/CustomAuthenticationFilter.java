package com.ensao.gi4.security.filter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ensao.gi4.security.config.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtConfig jwtConfig;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		return authenticationManager.authenticate(authenticationToken);

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException {

		String token = createToken(authResult);

		Map<String, String> tokenMap = new HashMap<>();
		tokenMap.put("token", token);
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		new ObjectMapper().writeValue(response.getOutputStream(), tokenMap);

	}

	private String createToken(Authentication authResult) {
		User user = (User) authResult.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
        return JWT.create().withSubject(user.getUsername()).withIssuedAt(new Date())
                .withExpiresAt(getTokenExpiration()).withIssuer("auth0").withClaim("roles", getRoles(user))
                .sign(algorithm);
	}

	private java.sql.Date getTokenExpiration() {
		return java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays()));
	}

	private List<String> getRoles(User user) {
		return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	}

}
