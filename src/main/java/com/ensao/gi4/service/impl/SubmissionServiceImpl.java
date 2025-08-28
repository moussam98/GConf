package com.ensao.gi4.service.impl;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.dto.SubmissionRequestDto;
import com.ensao.gi4.dto.mapper.SubmissionMapper;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.repository.SubmissionRepository;
import com.ensao.gi4.service.api.AuthorsService;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.service.api.DocumentService;
import com.ensao.gi4.service.api.SubmissionService;
import com.ensao.gi4.service.exception.ConferenceNotFoundException;
import com.ensao.gi4.utils.MessageSourceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

	private final SubmissionRepository submissionRepository;
	private final ConferenceService conferenceService;
	private final DocumentService documentService;
	private final AuthorsService authorService;
	private final MessageSourceUtils messageSourceUtils;
	private final SubmissionMapper submissionMapper;

	@Override
	public SubmissionDto createByConferenceId(Long conferenceId, SubmissionRequestDto submissionRequestDto){
		return conferenceService.findById(conferenceId)
				.map(conferenceDto -> saveSubmissionIfConferenceExists(conferenceDto, submissionRequestDto))
				.orElseThrow(() -> new ConferenceNotFoundException(messageSourceUtils
						.getMessage("participant.conference.not_found")));
	}

	@Override
	public Optional<SubmissionDto> findById(Long id) {
		return submissionRepository.findSubmissionById(id)
				.map(submissionMapper::toSubmissionDto);
	}

	@Override
	public List<SubmissionDto> listByConferenceId(Long conferenceId) {
		return submissionRepository.findSubmissionsByConferenceId(conferenceId)
				.stream().map(submissionMapper::toSubmissionDto).toList();
	}

	@Override
	public Boolean evaluate(Long submissionId, Boolean isValidate) {
		
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

	private SubmissionDto saveSubmissionIfConferenceExists(ConferenceDto conferenceDto,
														   SubmissionRequestDto submissionRequestDto){
		try {
			return submissionMapper.toSubmissionDto(saveSubmission(conferenceDto, submissionRequestDto));
		}catch (IOException e){
			throw new IllegalStateException("Error while saving submission: " +  e.getMessage());
		}
	}

	private Submission saveSubmission(ConferenceDto conferenceDto, SubmissionRequestDto submissionRequestDto)
			throws IOException {
		Submission submission = submissionMapper.toSubmission(submissionRequestDto, conferenceDto.id());
		documentService.add(submission.getDocument());
		submission.getAuthors().forEach(author -> {
			Instant instant = Instant.now();
			author.setCreatedAt(instant);
			author.setUpdatedAt(instant);
		});
		authorService.addAll(submission.getAuthors());
		submissionRepository.save(submission);
		return submission;
	}

	
}
