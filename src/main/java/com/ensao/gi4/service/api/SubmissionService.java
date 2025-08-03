package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.SubmissionRequestDto;
import com.ensao.gi4.model.Submission;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SubmissionService {
	
	Long add(SubmissionRequestDto submissionRequestDto, Long userId) throws IOException;
	Optional<Submission> findById(Long id); 
	Optional<List<Submission>> findAllSubmission(); 
	Boolean evaluateSubmission(Long submissionId, Boolean isValidate);
	Boolean deleteById(Long id); 
}
