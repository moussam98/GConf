package com.ensao.gi4.service.api;

import java.util.List;
import java.util.Optional;

import com.ensao.gi4.model.Submission;

public interface SubmissionService {
	
	int add(Submission submission); 
	Optional<Submission> findById(Long id); 
	Optional<List<Submission>> findAllSubmission(); 
	boolean existsById(Long id);

}
