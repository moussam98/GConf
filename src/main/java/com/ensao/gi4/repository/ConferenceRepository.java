package com.ensao.gi4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ensao.gi4.model.Conference;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
	boolean existsByName(String name);
	Optional<Conference> findByName(String name);
	Optional<Conference> findByAcronym(String acronym);
}
