package com.ensao.gi4.projection;

import com.ensao.gi4.model.Role;

import java.time.Instant;

public interface UserProjection {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    Role getRole();
    Instant getCreatedAt();
    Instant getUpdatedAt();
}

