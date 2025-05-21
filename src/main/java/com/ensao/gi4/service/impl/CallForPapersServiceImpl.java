package com.ensao.gi4.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.Tuple;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.User;
import com.ensao.gi4.repository.CallForPapersRepository;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.repository.TopicRepository;
import com.ensao.gi4.service.api.CallForPapersService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CallForPapersServiceImpl implements CallForPapersService {

	private final CallForPapersRepository callForPapersRepository;
	private final ConferenceRepository conferenceRepository;
	private final TopicRepository topicRepository; 
	

	@Override
	public Optional<CallForPapers> add(CallForPapersDto callForPapersDto, Long conferenceId) throws JsonProcessingException {

		Optional<Conference> optionalConference = conferenceRepository.findById(conferenceId);
		boolean exists = optionalConference.isPresent();

		if (exists) {
			CallForPapers callForPapers = Mapper.toCallForPapers(callForPapersDto); 
			callForPapers.setConference(optionalConference.get());
			topicRepository.saveAll(callForPapers.getTopics());
			callForPapersRepository.save(callForPapers);
			
			return Optional.of(callForPapers);
		} else {
			return Optional.empty();
		}

	}

	@Override
	public Optional<CallForPapers> findByConferenceId(Long confernceId) {
		
		List<Tuple> tuples = callForPapersRepository.findCfpById(confernceId); 
		CallForPapers callForPapers; 

		if (!tuples.isEmpty()) {
			callForPapers = mappedCallForPapers(tuples);
			return Optional.of(callForPapers);
		}else {
			
			Optional<Conference> optionalConference = conferenceRepository.findById(confernceId);
			boolean exists = optionalConference.isPresent(); 
	
			return exists ? callForPapersRepository.findByConference(optionalConference.get()) : Optional.empty();
		}

	}

	@Override
	public boolean existsByConferenceId(Long confernceId) {
		
		Optional<Conference> optionalConference = conferenceRepository.findById(confernceId);

        return optionalConference.filter(callForPapersRepository::existsByConference).isPresent();

    }
	
	private CallForPapers mappedCallForPapers(List<Tuple> tuples) {
		CallForPapers callForPapers;
		Conference conference;
		callForPapers = tuples.getFirst().get(0, CallForPapers.class);
		for (Tuple tuple : tuples) {
			if (tuple.get(1) != null) {
				conference = conferenceMapper(tuple);
				callForPapers.setConference(conference);
			}
		}
		return callForPapers;
	}

	private Conference conferenceMapper(Tuple tuple) {
		Conference conference;
		conference = new Conference();
		conference.setId(tuple.get(1, Long.class));
		conference.setName(tuple.get(2, String.class));
		conference.setAcronym(tuple.get(3, String.class));
		conference.setVenue(tuple.get(4, String.class));
		conference.setCity(tuple.get(5, String.class));
		conference.setCountry(tuple.get(6, String.class));
		conference.setFirstDay(tuple.get(7, LocalDate.class));
		conference.setLastDay(tuple.get(8, LocalDate.class));
		conference.setPrimaryArea(tuple.get(9, String.class));
		conference.setSecondaryArea(tuple.get(10, String.class));
		conference.setOrganizer(tuple.get(11, String.class));
		conference.setPhoneNumber(tuple.get(12, String.class));
		conference.setOtherInfo(tuple.get(13, String.class));
		conference.setUser(tuple.get(14, User.class));
		return conference;
	}

}
