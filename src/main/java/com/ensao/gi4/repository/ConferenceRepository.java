package com.ensao.gi4.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.User;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
	boolean existsByName(String name);

	boolean existsByAcronym(String acronym);

	@Query("SELECT conf, sub.id, sub.title, sub.description, sub.document.id, sub.document.filename, sub.document.fileType "
			+ "FROM Conference conf LEFT JOIN conf.submissions sub WHERE conf.id = :id")
	List<Tuple> findConferenceById(Long id);

	@Query("SELECT conf, sub.id, sub.title, sub.description, sub.document.id, sub.document.filename, sub.document.fileType "
			+ "FROM Conference conf LEFT JOIN conf.submissions sub WHERE conf.name = :name")
	List<Tuple> findConferenceByName(String name);

	Optional<Conference> findByName(String name);

	@Query("SELECT conf, sub.id, sub.title, sub.description, sub.document.id, sub.document.filename, sub.document.fileType "
			+ "FROM Conference conf LEFT JOIN conf.submissions sub WHERE conf.acronym = :acronym")
	List<Tuple> findConferenceByAcronym(String acronym);

	Optional<Conference> findByAcronym(String acronym);

	@Query("SELECT conf, sub.id, sub.title, sub.description, sub.document.id, sub.document.filename, sub.document.fileType "
			+ "FROM Conference conf LEFT JOIN conf.submissions sub WHERE conf.user = :user")
	List<Tuple> findConferenceByUser(User user);

	Optional<Conference> findByUser(User user);

}
