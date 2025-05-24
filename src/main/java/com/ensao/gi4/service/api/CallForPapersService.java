package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.model.CallForPapers;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface CallForPapersService {
	Optional<CallForPapers> add(CallForPapersDto callForPapersDto, Long conferenceId) throws JsonProcessingException;
	Optional<CallForPapers> findByConferenceId(Long conferenceId);
	boolean existsByConferenceId(Long conferenceId);

}
