package com.ensao.gi4.repository;

import com.ensao.gi4.model.Submission;
import com.ensao.gi4.projection.SubmissionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
	Optional<SubmissionProjection> findSubmissionById(Long id);
	@Query("SELECT s FROM Submission s WHERE s.conference.id = :conferenceId")
	List<SubmissionProjection> findSubmissionsByConferenceId(Long conferenceId);
}
