package com.ensao.gi4.repository;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.projection.CallForPapersProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CallForPapersRepository extends JpaRepository<CallForPapers, Long> {
	
	Optional<CallForPapersProjection> findByConference(Conference conference);
}
