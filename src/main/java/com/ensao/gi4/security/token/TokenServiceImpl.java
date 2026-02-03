package com.ensao.gi4.security.token;

import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.dto.mapper.UserMapper;
import com.ensao.gi4.security.jwt.JwtService;
import com.ensao.gi4.service.api.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class TokenServiceImpl implements TokenService, InitializingBean, DisposableBean {

    private static final Log LOGGING = LogFactory.getLog(TokenServiceImpl.class);
    private static final String CLEANUP_EXPIRED_TOKENS_CRON_EXPRESSION = "@hourly";

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ThreadPoolTaskScheduler taskScheduler;


    public TokenServiceImpl(JwtService jwtService, TokenRepository tokenRepository, UserService
            userService, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskScheduler = createThreadPoolTaskScheduler();
    }

    private ThreadPoolTaskScheduler createThreadPoolTaskScheduler() {
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("Gconf-tokens-");
        scheduler.initialize();
        scheduler.schedule(this::cleanUpExpiredTokens,
                new CronTrigger(CLEANUP_EXPIRED_TOKENS_CRON_EXPRESSION));
        return scheduler;
    }

    private void cleanUpExpiredTokens() {
        int deletedCount = tokenRepository.cleanUpExpiredToken(Instant.now());
        if (LOGGING.isDebugEnabled()) {
            LOGGING.debug(String.format("Cleaned up %d expired tokens", deletedCount));
        }
    }

    @Override
    public void destroy() {
        taskScheduler.destroy();
    }

    @Override
    public void afterPropertiesSet() {
        taskScheduler.afterPropertiesSet();
    }

    @Override
    public String generateAccessToken(String username) {
        return jwtService.generateAccessToken(createUserClaims(username), username);
    }

    @Override
    public String generateRefreshToken(String username) {
        return jwtService.generateRefreshToken(createUserClaims(username) ,username);
    }


    private Map<String, Object> createUserClaims(String subject) {
        var user = userService.getByEmail(subject);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", List.of(user.role().name()));
        claims.put("userId", user.id());
        return claims;
    }

    public void saveUserTokens(String subject, String accessToken, String refreshToken) {
        var userDto = userService.getByEmail(subject);
        revokeAllUserTokens(userDto.id());
        saveToken(userDto, accessToken, TokenType.ACCESS, jwtService.jwtProperties().getTokenExpirationInMilliseconds());
        saveToken(userDto, refreshToken, TokenType.REFRESH, jwtService.jwtProperties().getTokenExpirationInMilliseconds());
    }

    private void saveToken(UserDto userDto, String jwtToken, TokenType tokenType, long tokenExpiration) {
        var token = Token.builder()
                .user(userMapper.toUser(userDto))
                .tokenValue(jwtToken)
                .tokenType(tokenType)
                .expiredAt(Instant.now().plusMillis(tokenExpiration))
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Long userId) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(userId, Instant.now());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(validUserTokens);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        return jwtService.isRefreshTokenValid(refreshToken) && this.isNotRevoked(refreshToken);
    }

    public String extractSubject(String token) {
        return jwtService.extractUsername(token);
    }

    private boolean isNotRevoked(String token) {
        return !tokenRepository.findByTokenValue(token).map(Token::isRevoked).orElseThrow(()
                -> new InvalidTokenException("Invalid token"));
    }

    @Override
    public boolean isTokenValid(String token, String expectedSubject) {
        return jwtService.isTokenValid(token, expectedSubject) && this.isNotRevoked(token);
    }
}