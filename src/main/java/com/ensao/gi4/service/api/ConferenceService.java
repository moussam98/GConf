package com.ensao.gi4.service.api;

import java.util.Optional;

import com.ensao.gi4.model.Conference;

public interface ConferenceService {
	int add(Conference conference); 
	Optional<Conference> findByName(String Name);
	Optional<Conference> findByAcronym(String acronym);
	// TODO adding methods :  update and delete
}
