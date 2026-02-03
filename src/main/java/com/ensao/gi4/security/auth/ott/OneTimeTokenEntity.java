package com.ensao.gi4.security.auth.ott;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "one_time_tokens")
@Getter
@Setter
class OneTimeTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String tokenValue;
    private String username;
    private Instant expiresAt;

    protected OneTimeTokenEntity() {
    }

    OneTimeTokenEntity(String tokenValue, String username, Instant expiresAt) {
        this.tokenValue = tokenValue;
        this.username = username;
        this.expiresAt = expiresAt;
    }

}
