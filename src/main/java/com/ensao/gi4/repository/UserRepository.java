package com.ensao.gi4.repository;

import com.ensao.gi4.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Integer deleteByEmail(String email);
	boolean existsByEmail(String email);
}
