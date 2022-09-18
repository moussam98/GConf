package com.ensao.gi4.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.model.User;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.repository.UserRepository;
import com.ensao.gi4.service.api.ConferenceService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

	private final ConferenceRepository conferenceRepository;
	private final UserRepository userRepository;

	@Override
	public Long add(ConferenceDto conferenceDto, Long userId) {
		Conference conference = Mapper.toConference(conferenceDto);

		boolean existsByName = conferenceRepository.existsByName(conference.getName());
		boolean existsByAcronym = conferenceRepository.existsByAcronym(conference.getAcronym());

		if (existsByName && existsByAcronym) {
			return -1l;
		} else {
			Optional<User> userOptional = userRepository.findById(userId);
			conference.setUser(userOptional.get());
			return conferenceRepository.save(conference).getId();
		}
	}

	@Override
	public Optional<Conference> findByName(String name) {
		List<Tuple> tuples = conferenceRepository.findConferenceByName(name);
		if (!tuples.isEmpty()) {
			return mappedConference(tuples);
		}else {
			return conferenceRepository.findByName(name);
		}

	}

	
	@Override
	public Optional<Conference> findByAcronym(String acronym) {
		List<Tuple> tuples = conferenceRepository.findConferenceByAcronym(acronym);
		if (!tuples.isEmpty()) {
			return mappedConference(tuples);
		}else {
			return conferenceRepository.findByAcronym(acronym);
		}
	}

	@Override
	public Optional<Conference> findById(Long id) {
		List<Tuple> tuples = conferenceRepository.findConferenceById(id);
		if (!tuples.isEmpty()) {
			return mappedConference(tuples);
		}else {
			return conferenceRepository.findById(id);
		}
	}

	@Override
	public boolean existsById(Long id) {
		return conferenceRepository.existsById(id);
	}

	@Override
	public boolean updateConferenceById(Long id, ConferenceDto conferenceDto) {

		Conference newConference = Mapper.toConference(conferenceDto);

		Optional<Conference> conferenceOptional = conferenceRepository.findById(id);

		if (conferenceOptional.isPresent()) {
			updateConference(newConference, conferenceOptional.get());
			return true;
		}else {
			return false;			
		}
	}

	@Override
	public Boolean deleteById(Long id) {
		conferenceRepository.deleteById(id);
		return true;
	}
	
	private void updateConference(Conference newConference, Conference updatedConference) {
		updatedConference.setName(newConference.getName());
		updatedConference.setAcronym(newConference.getAcronym());
		updatedConference.setVenue(newConference.getVenue());
		updatedConference.setCity(newConference.getCity());
		updatedConference.setCountry(newConference.getCountry());
		updatedConference.setFirstDay(newConference.getFirstDay());
		updatedConference.setLastDay(newConference.getLastDay());
		updatedConference.setPrimaryArea(newConference.getPrimaryArea());
		updatedConference.setSecondaryArea(newConference.getSecondaryArea());
		updatedConference.setOrganizer(newConference.getOrganizer());
		updatedConference.setPhoneNumber(newConference.getPhoneNumber());
		updatedConference.setOtherInfo(newConference.getOtherInfo());
	}
	
	private Optional<Conference> mappedConference(List<Tuple> tuples) {
		Conference conference;
		List<Submission> submissions;
		conference = tuples.get(0).get(0, Conference.class);
		submissions = new ArrayList<>(); 
		for (Tuple tuple : tuples) {
			
			if (tuple.get(1) != null) {
				Submission submission = submissionMapper(tuple);
				Document document = documentMapper(tuple);
				submission.setDocument(document);
				submissions.add(submission); 
			}
		}
		conference.setSubmissions(submissions);
		return Optional.of(conference);
	}

	private Submission submissionMapper(Tuple tuple) {
		Submission submission = new Submission();
		submission.setId(tuple.get(1, Long.class));
		submission.setTitle(tuple.get(2, String.class));
		submission.setDescription(tuple.get(3, String.class));
		return submission;
	}

	private Document documentMapper(Tuple tuple) {
		Document document = new Document();
		document.setId(tuple.get(4, Long.class));
		document.setFilename(tuple.get(5, String.class));
		document.setFileType(tuple.get(6, String.class));
		return document;
	}

}
