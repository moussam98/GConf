package com.ensao.gi4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;

@Repository
public interface CallForPapersRepository extends JpaRepository<CallForPapers, Long> {
	
	Optional<CallForPapers> findByConference(Conference conference);
	boolean existsByConference(Conference conference);
}
