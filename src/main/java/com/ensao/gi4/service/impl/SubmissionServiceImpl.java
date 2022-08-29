package com.ensao.gi4.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensao.gi4.model.Submission;
import com.ensao.gi4.repository.SubmissionRepository;
import com.ensao.gi4.service.api.SubmissionService;

@Service
public class SubmissionServiceImpl implements SubmissionService {

	private SubmissionRepository submissionRepository; 
	
	@Autowired
	public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
		this.submissionRepository = submissionRepository;
	}

	@Override
	public int add(Submission submission) {
		submissionRepository.save(submission); 
		return 1;
	}

	@Override
	public Optional<Submission> findById(Long id) {
		return submissionRepository.findById(id);
	}

	@Override
	public Optional<List<Submission>> findAllSubmission() {
		return Optional.of(submissionRepository.findAll());
	}

	@Override
	public boolean existsById(Long id) {
		return submissionRepository.existsById(id);
	}

}
