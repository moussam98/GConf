package com.ensao.gi4.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expiredAt < :now or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(@Param("id") Long id, @Param("now") Instant now);

    Optional<Token> findByTokenValue(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expiredAt < :now")
    int cleanUpExpiredToken(@Param("now") Instant now);
}