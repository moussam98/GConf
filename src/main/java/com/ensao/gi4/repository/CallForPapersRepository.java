package com.ensao.gi4.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;

@Repository
public interface CallForPapersRepository extends JpaRepository<CallForPapers, Long> {
	
	Optional<CallForPapers> findByConference(Conference conference);
	boolean existsByConference(Conference conference);
	@Query("SELECT cfp, conf.id, conf.name, conf.acronym, conf.venue, conf.city, conf.country, "
				+ "conf.firstDay, conf.lastDay, conf.primaryArea, conf.secondaryArea, conf.organizer, "
				+ "conf.phoneNumber, conf.otherInfo, conf.user "
			+ "FROM CallForPapers cfp LEFT JOIN cfp.conference conf WHERE conf.id = :id")
	List<Tuple> findCfpById(Long id); 
}
