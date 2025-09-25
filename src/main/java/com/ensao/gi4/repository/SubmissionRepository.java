package com.ensao.gi4.repository;

import com.ensao.gi4.dto.SubmissionStatisticsDto;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.projection.SubmissionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT s FROM Submission s WHERE s.conference.id = :conferenceId")
    Page<SubmissionProjection> findSubmissionsByConferenceId(Long conferenceId, Pageable pageable);

    @Query("""
            SELECT new com.ensao.gi4.dto.SubmissionStatisticsDto(
                		COUNT(s.id),
                		COUNT(CASE WHEN s.isValidate = true THEN 1 END),
                		COUNT(CASE WHEN s.isEvaluate = false THEN 1 END),
                		COUNT(CASE WHEN s.isEvaluate = true AND s.isValidate = false THEN 1 END))
            FROM Submission s WHERE s.conference.id = :conferenceId""")
	SubmissionStatisticsDto findSubmissionStatisticsByConferenceId(Long conferenceId);


}


