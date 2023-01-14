package com.ensao.gi4.service.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.model.Submission;

public interface SubmissionService {
	
	Long add(SubmissionDto submissionDto, Long userId) throws IOException; 
	Optional<Submission> findById(Long id); 
	Optional<List<Submission>> findAllSubmission(); 
	Boolean existsById(Long id);
	Boolean evaluateSubmission(Long submissionId, Boolean isValidate); 
	Boolean deleteById(Long id); 
}
