package com.ensao.gi4.service.api;

import java.util.Optional;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferenceFirstInfoDto;
import com.ensao.gi4.model.Conference;

public interface ConferenceService {
	Long add(ConferenceFirstInfoDto conferenceFirstInfoDto, Long userId); 
	Optional<Conference> findByName(String Name);
	Optional<Conference> findByAcronym(String acronym);
	Optional<Conference> findById(Long id);
	Optional<Conference> findByUser(Long userId); 
	boolean existsById(Long id); 
	Optional<Conference> updateConferenceById(Long id, ConferenceDto conferenceDto); 
	Boolean deleteById(Long id);
}
