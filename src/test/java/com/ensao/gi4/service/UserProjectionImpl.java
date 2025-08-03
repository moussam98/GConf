package com.ensao.gi4.service;

import com.ensao.gi4.model.Role;
import com.ensao.gi4.model.User;
import com.ensao.gi4.projection.UserProjection;

import java.time.Instant;

public record UserProjectionImpl(User user) implements UserProjection {

    @Override
    public Long getId() {
        return user.getId();
    }

    @Override
    public String getFirstName() {
        return user.getFirstName();
    }

    @Override
    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public Role getRole() {
        return user.getRole();
    }

    @Override
    public Instant getCreatedAt() {
        return user.getCreatedAt();
    }

    @Override
    public Instant getUpdatedAt() {
        return user.getUpdatedAt();
    }

}
