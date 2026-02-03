package com.ensao.gi4.security.auth.ott;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.authentication.ott.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.List;


@Service
final class OneTimeTokenServiceImpl implements OneTimeTokenService, InitializingBean, DisposableBean {

    public static final String CLEANUP_EXPIRED_ONE_TIME_TOKEN_CRON_EXPRESSION = "@hourly";
    private final Log logger = LogFactory.getLog(this.getClass());

    private final Clock clock = Clock.systemUTC();
    private static final SecureRandom RANDOM = new SecureRandom();
    private final OneTimeTokenRepository oneTimeTokenRepository;
    private final ThreadPoolTaskScheduler taskScheduler;

    OneTimeTokenServiceImpl(OneTimeTokenRepository oneTimeTokenRepository) {
        this.oneTimeTokenRepository = oneTimeTokenRepository;
        taskScheduler = createTaskScheduler();
    }

    @Override
    @NonNull
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
        var tokenValue = String.valueOf(100000 + RANDOM.nextInt(900000)); // 6 digits
        Instant fiveMinutesFromNow = this.clock.instant().plusSeconds(300);
        OneTimeToken ott = new DefaultOneTimeToken(tokenValue, request.getUsername(), fiveMinutesFromNow);
        oneTimeTokenRepository.save(new OneTimeTokenEntity(tokenValue, request.getUsername(), fiveMinutesFromNow));
        return ott;
    }


    @Override
    public OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
        List<OneTimeTokenEntity> tokens = oneTimeTokenRepository
                .findByTokenValue(authenticationToken.getTokenValue());
        if (CollectionUtils.isEmpty(tokens)) {
            return null;
        }

        OneTimeTokenEntity token = tokens.getFirst();
        oneTimeTokenRepository.delete(token);
        if (isExpired(token)) {
            return null;
        }

        return new DefaultOneTimeToken(token.getTokenValue(), token.getUsername(), token.getExpiresAt());
    }

    private boolean isExpired(OneTimeTokenEntity value) {
        return clock.instant().isAfter(value.getExpiresAt());
    }


    private ThreadPoolTaskScheduler createTaskScheduler() {
        var taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("Gconf-one-time-tokens-");
        taskScheduler.initialize();
        taskScheduler.schedule(this::cleanupExpiredTokens,
                new CronTrigger(OneTimeTokenServiceImpl.CLEANUP_EXPIRED_ONE_TIME_TOKEN_CRON_EXPRESSION));
        return taskScheduler;
    }

    private void cleanupExpiredTokens() {
        int deletedCount = oneTimeTokenRepository.cleanUpExpiredTokens(Instant.now());
        if (logger.isDebugEnabled()) {
            logger.debug("Cleaned up " + deletedCount +  " expired tokens");
        }
    }

    @Override
    public void afterPropertiesSet() {
        taskScheduler.afterPropertiesSet();
    }

    @Override
    public void destroy() {
        taskScheduler.destroy();
    }
}
