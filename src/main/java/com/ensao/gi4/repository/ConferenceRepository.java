package com.ensao.gi4.repository;

import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.User;
import com.ensao.gi4.projection.ConferenceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
	Optional<ConferenceProjection> findConferenceById(Long id);

	Optional<ConferenceProjection> findByOwner(User owner);

	boolean existsByNameAndAcronym(String name, String acronym);
}