package com.ensao.gi4.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.repository.AuthorRepository;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.repository.DocumentRepository;
import com.ensao.gi4.repository.KeywordRepository;
import com.ensao.gi4.repository.SubmissionRepository;
import com.ensao.gi4.service.api.SubmissionService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

	private final SubmissionRepository submissionRepository;
	private final ConferenceRepository conferenceRepository;
	private final KeywordRepository keywordRepository;
	private final DocumentRepository documentRepository;
	private final AuthorRepository authorRepository;

	@Override
	public Long add(SubmissionDto submissionDto, Long conferenceId) throws IOException {

		Optional<Conference> conferenceOptional = conferenceRepository.findById(conferenceId);

		if (conferenceOptional.isPresent()) {

			Submission submission = Mapper.toSubmission(submissionDto);
			keywordRepository.saveAll(submission.getKeywords());
			documentRepository.save(submission.getDocument());
			submission.setConference(conferenceOptional.get());
			submissionRepository.save(submission);
			authorRepository.saveAll(submission.getAuthors());

			return submission.getId();
		} else {
			return -1l;
		}
	}

	@Override
	public Optional<Submission> findById(Long id) {

		List<Tuple> tuples = submissionRepository.findSubmissionById(id);
		Submission submission;
		Document document;

		if (!tuples.isEmpty()) {
			submission = tuples.get(0).get(0, Submission.class);
			if (tuples.get(0).get(1) != null) {
				document = new Document();
				document.setId(tuples.get(0).get(1, Long.class));
				document.setFilename(tuples.get(0).get(2, String.class));
				document.setFileType(tuples.get(0).get(3, String.class));
				submission.setDocument(document);
			}
			
			return Optional.of(submission);
		}

		return Optional.empty();
	}

	@Override
	public Optional<List<Submission>> findAllSubmission() {
		
		List<Tuple> tuples = submissionRepository.findAllSubmission();
		List<Submission> submissions = new ArrayList<>(); 
		Submission submission; 
		Document document; 
		
		for (Tuple tuple : tuples) {
			submission = tuple.get(0, Submission.class); 
			if (tuple.get(1) != null) {
				document = new Document(); 
				document.setId(tuple.get(1, Long.class));
				document.setFilename(tuple.get(2, String.class));
				document.setFileType(tuple.get(3, String.class));
				submission.setDocument(document);
			}
			submissions.add(submission);
		}
		
		return Optional.of(submissions);
	}

	@Override
	public boolean existsById(Long id) {
		return submissionRepository.existsById(id);
	}

}
