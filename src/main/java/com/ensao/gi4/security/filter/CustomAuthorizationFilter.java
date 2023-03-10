package com.ensao.gi4.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ensao.gi4.security.config.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}

		try {

			DecodedJWT decodedJWT = getDecodedJWT(authorizationHeader);
			String username = decodedJWT.getSubject();
			Collection<GrantedAuthority> authorities = getAuthorities(decodedJWT);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
					null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			filterChain.doFilter(request, response);

		} catch (Exception exception) {

			log.error("Error logging in : {} ", exception.getMessage());
			Map<String, String> error = new HashMap<>();
			error.put("error_message", exception.getMessage());
			response.setStatus(HttpStatus.FORBIDDEN.value());
			new ObjectMapper().writeValue(response.getOutputStream(), error);
		}

	}

	private Collection<GrantedAuthority> getAuthorities(DecodedJWT decodedJWT) {
		String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
		return authorities;
	}

	private DecodedJWT getDecodedJWT(String authorizationHeader) {
		String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
		Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(token);
		return decodedJWT;
	}

}
