package com.ensao.gi4.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensao.gi4.model.Conference;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.service.api.ConferenceService;

@Service
public class ConferenceServiceImpl implements ConferenceService {

	private ConferenceRepository conferenceRepository;

	@Autowired
	public ConferenceServiceImpl(ConferenceRepository conferenceRepository) {
		this.conferenceRepository = conferenceRepository;
	}

	@Override
	public int add(Conference conference) {
		boolean exits = conferenceRepository.existsByName(conference.getName());

		if (!exits) {
			conferenceRepository.save(conference);
			return 1;
		}

		return 0;
	}

	@Override
	public Optional<Conference> findByName(String name) {
		return conferenceRepository.findByName(name);
	}

	@Override
	public Optional<Conference> findByAcronym(String acronym) {
		return conferenceRepository.findByAcronym(acronym);
	}

}
