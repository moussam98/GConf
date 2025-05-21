package com.ensao.gi4.repository;

import java.util.List;

import jakarta.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ensao.gi4.model.Submission;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

	@Query("SELECT sub, doc.id, doc.filename, doc.fileType "
			+ "FROM Submission sub LEFT JOIN sub.document doc WHERE sub.id = :id")
	List<Tuple> findSubmissionById(Long id);

	@Query("SELECT sub, doc.id, doc.filename, doc.fileType FROM Submission sub LEFT JOIN sub.document doc")
	List<Tuple> findAllSubmission();
}
