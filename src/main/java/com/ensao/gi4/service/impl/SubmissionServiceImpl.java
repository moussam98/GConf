package com.ensao.gi4.service.impl;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.dto.UserResponseDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.repository.SubmissionRepository;
import com.ensao.gi4.service.api.*;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

	private final SubmissionRepository submissionRepository;
	private final ConferenceService conferenceService;
	private final KeywordService keywordService;
	private final DocumentService documentService;
	private final AuthorsService authorService;
	private final UserService userService;

	@Override
	public Long add(SubmissionDto submissionDto, Long userId) throws IOException {
		return userService.findById(userId)
				.map(user -> saveSubmissionIfConferenceExists(user, submissionDto))
				.orElse(-1L);
	}

	@Override
	public Optional<Submission> findById(Long id) {
		List<Tuple> tuples = submissionRepository.findSubmissionById(id);
		
		if (!tuples.isEmpty()) {
			Submission submission = submissionMapper(tuples);
			return Optional.of(submission);
		}

		return Optional.empty();
	}

	@Override
	public Optional<List<Submission>> findAllSubmission() {
		
		List<Tuple> tuples = submissionRepository.findAllSubmission();
		List<Submission> submissions = submissionsMapper(tuples);
	
		return Optional.of(submissions);
	}

	@Override
	public Boolean evaluateSubmission(Long submissionId, Boolean isValidate) {
		
		Optional<Submission> optionalSubmission = submissionRepository.findById(submissionId);
		
		if (optionalSubmission.isPresent()) {
			Submission submission = optionalSubmission.get();
			submission.setIsEvaluate(true);
			submission.setIsValidate(isValidate);
			return submission.getIsEvaluate(); 
		}else {
			return false;			
		}
	}

	@Override
	public Boolean deleteById(Long id) {
		submissionRepository.deleteById(id);
		return true;
	}
	
	private List<Submission> submissionsMapper(List<Tuple> tuples) {
		List<Submission> submissions = new ArrayList<>(); 
		
		for (Tuple tuple : tuples) {
			Submission submission = submissionMapper(tuple);
			submissions.add(submission);
		}
		return submissions;
	}

	private Submission submissionMapper(Tuple tuple) {
		Submission submission = tuple.get(0, Submission.class); 
		if (tuple.get(1) != null) {
			Document document = documentMapper(tuple);
			submission.setDocument(document);
		}
		return submission;
	}
	
	private Submission submissionMapper(List<Tuple> tuples) {
		Submission submission = tuples.getFirst().get(0, Submission.class);
		if (tuples.getFirst().get(1) != null) {
			Document document = documentMapper(tuples);
			submission.setDocument(document);
		}
		return submission;
	}

	private Document documentMapper(Tuple tuple) {
		Document document;
		document = new Document(); 
		document.setId(tuple.get(1, Long.class));
		document.setFilename(tuple.get(2, String.class));
		document.setFileType(tuple.get(3, String.class));
		return document;
	}

	private Document documentMapper(List<Tuple> tuples) {
		Document document;
		document = new Document();
		document.setId(tuples.getFirst().get(1, Long.class));
		document.setFilename(tuples.getFirst().get(2, String.class));
		document.setFileType(tuples.getFirst().get(3, String.class));
		return document;
	}
	
	private Long saveSubmissionIfConferenceExists(UserResponseDto userResponseDto, SubmissionDto submissionDto){
		return conferenceService.findByUser(userResponseDto.id())
				.map(conference -> {
                    try {
                        return saveSubmission(conference, submissionDto).getId();
                    } catch (IOException e) {
                        throw new IllegalStateException("Error while saving submission", e);
                    }
                })
				.orElse(-1L);
	}

	private Submission saveSubmission(Conference conference,  SubmissionDto submissionDto)
			throws IOException {
		Submission submission = Mapper.toSubmission(submissionDto);
		keywordService.addAll(submission.getKeywords());
		documentService.add(submission.getDocument());
		submission.setConference(conference);
		authorService.addAll(submission.getAuthors());
		submissionRepository.save(submission);
		return submission;
	}

	
}
