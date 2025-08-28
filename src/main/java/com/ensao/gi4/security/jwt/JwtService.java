package com.ensao.gi4.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public record JwtService(JwtProperties jwtProperties) {

    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, String username) {
        extraClaims.put(TOKEN_TYPE, ACCESS_TOKEN);
        return buildToken(extraClaims, username, jwtProperties.getTokenExpirationInMilliseconds());
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, String username) {
        extraClaims.put(TOKEN_TYPE, REFRESH_TOKEN);
        return buildToken(Map.of(TOKEN_TYPE, REFRESH_TOKEN), username,
                jwtProperties.getTokenRefreshExpirationInMilliseconds());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String username,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String  expectedUsername) {
        final String username = extractUsername(token);
        return (username.equals(expectedUsername)) && isTokenNotExpired(token);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        return (!extractUsername(refreshToken).isEmpty()) &&
               isTokenNotExpired(refreshToken) &&
               isRefreshToken(refreshToken);
    }

    private boolean isRefreshToken(String token) {
        final Claims claims = extractAllClaims(token);
        return Objects.equals(claims.get(TOKEN_TYPE, String.class), REFRESH_TOKEN);
    }

    private boolean isTokenNotExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}