package com.ensao.gi4.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.repository.CallForPapersRepository;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.service.api.CallForPapersService;

@Service
public class CallForPapersServiceImpl implements CallForPapersService {

	private CallForPapersRepository callForPapersRepository;
	private ConferenceRepository conferenceRepository;

	@Autowired
	public CallForPapersServiceImpl(CallForPapersRepository callForPapersRepository,
			ConferenceRepository conferenceRepository) {
		this.callForPapersRepository = callForPapersRepository;
		this.conferenceRepository = conferenceRepository;
	}

	@Transactional
	@Override
	public int add(CallForPapers callForPapers, Long conferenceId) {

		boolean exists = conferenceRepository.existsById(conferenceId);

		if (exists) {
			Optional<Conference> conferenceOptional = conferenceRepository.findById(conferenceId);
			callForPapers.setConference(conferenceOptional.get());
			conferenceOptional.get().setCallForPapers(callForPapers);
			callForPapersRepository.save(callForPapers);
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public Optional<CallForPapers> findByConferenceId(Long confernceId) {

		boolean exists = conferenceRepository.existsById(confernceId);

		if (exists) {
			Optional<Conference> conferenceOptional = conferenceRepository.findById(confernceId);
			return callForPapersRepository.findByConference(conferenceOptional.get());
		}

		return Optional.empty();
	}

	@Override
	public boolean existsByConferenceId(Long confernceId) {
		
		boolean exists = conferenceRepository.existsById(confernceId);
		
		if (exists) {
			Optional<Conference> conferenceOptional = conferenceRepository.findById(confernceId); 
			return callForPapersRepository.existsByConference(conferenceOptional.get()); 
		}
		
		return false;
	}

}
