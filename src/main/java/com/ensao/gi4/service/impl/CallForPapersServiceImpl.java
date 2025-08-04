package com.ensao.gi4.service.impl;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.dto.CallForPapersRequestDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.repository.CallForPapersRepository;
import com.ensao.gi4.service.api.CallForPapersService;
import com.ensao.gi4.service.api.ConferenceService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record CallForPapersServiceImpl(
		CallForPapersRepository callForPapersRepository,
		ConferenceService conferenceService
) implements CallForPapersService {



	@Override
	public Optional<CallForPapersDto> add(CallForPapersRequestDto callForPapersRequestDto, Long conferenceId){
		return conferenceService.findById(conferenceId)
				.map(conferenceDto -> {
					CallForPapers callForPapers = Mapper.toCallForPapers(callForPapersRequestDto);
					var conference = new Conference();
					conference.setId(conferenceId);
					callForPapers.setConference(conference);
					return Mapper.toCallForPapersDto(callForPapersRepository.save(callForPapers));
                });
	}

	@Override
	public Optional<CallForPapersDto> findByConferenceId(Long conferenceId) {
		var conference = new Conference();
		conference.setId(conferenceId);
		return callForPapersRepository.findByConference(conference)
				.map(Mapper::toCallForPapersDto);
	}

}
