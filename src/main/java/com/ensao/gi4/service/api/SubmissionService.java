package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.dto.SubmissionRequestDto;

import java.util.List;
import java.util.Optional;

public interface SubmissionService {
	
	SubmissionDto createByConferenceId(Long conferenceId, SubmissionRequestDto submissionRequestDto);
	Optional<SubmissionDto> findById(Long id);
	List<SubmissionDto> listByConferenceId(Long conferenceId);
	Boolean evaluate(Long submissionId, Boolean isValidate);
	Boolean deleteById(Long id);
}
