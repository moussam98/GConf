package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferenceFirstInfoDto;
import com.ensao.gi4.model.Conference;

import java.util.Optional;

public interface ConferenceService {
	Long add(ConferenceFirstInfoDto conferenceFirstInfoDto, Long userId); 
	Optional<Conference> findByName(String Name);
	Optional<Conference> findByAcronym(String acronym);
	Optional<Conference> findById(Long id);
	Optional<Conference> findByUser(Long userId); 
	Optional<Conference> updateConferenceById(Long id, ConferenceDto conferenceDto);
	void deleteById(Long id);
}
