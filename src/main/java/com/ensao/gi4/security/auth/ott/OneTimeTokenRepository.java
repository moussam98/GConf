package com.ensao.gi4.security.auth.ott;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
interface OneTimeTokenRepository extends JpaRepository<OneTimeTokenEntity, Long> {
    List<OneTimeTokenEntity> findByTokenValue(String tokenValue);

    @Modifying
    @Transactional
    @Query("DELETE FROM OneTimeTokenEntity ott WHERE ott.expiresAt < :instant")
    int cleanUpExpiredTokens(Instant instant);
}
