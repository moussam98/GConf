package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.dto.SubmissionRequestDto;
import com.ensao.gi4.dto.SubmissionStatisticsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SubmissionService {
	
	SubmissionDto createByConferenceId(Long conferenceId, SubmissionRequestDto submissionRequestDto);
	Optional<SubmissionDto> findById(Long id);
	List<SubmissionDto> listByConferenceId(Long conferenceId);
	Page<SubmissionDto> listByConferenceId(Long conferenceId, Pageable pageable);
	Boolean evaluate(Long submissionId, Boolean isValidate);
	Boolean deleteById(Long id);
	SubmissionStatisticsDto getSubmissionStatisticsByConferenceId(Long conferenceId);
}
