package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferencePatchDto;
import com.ensao.gi4.dto.ConferenceRequestDto;

import java.util.Optional;

public interface ConferenceService {
	ConferenceDto add(ConferenceRequestDto conferenceRequestDto, Long userId);
	Optional<ConferenceDto> findById(Long id);
	Optional<ConferenceDto> findByOwnerId(Long userId);
	Optional<ConferenceDto> updateConferenceById(Long id, ConferencePatchDto conferencePatchDto);
	void deleteById(Long id);
}
