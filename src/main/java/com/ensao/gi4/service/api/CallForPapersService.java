package com.ensao.gi4.service.api;

import java.util.Optional;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.model.CallForPapers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface CallForPapersService {
	Optional<CallForPapers> add(CallForPapersDto callForPapersDto, Long confernceId) throws JsonMappingException, JsonProcessingException;
	Optional<CallForPapers> findByConferenceId(Long confernceId); 
	boolean existsByConferenceId(Long confernceId);

}
