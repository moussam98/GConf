package com.ensao.gi4.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.service.api.SubmissionService;

@RestController
@RequestMapping("/api/v1/submission")
public class SubmissionController {

	@Autowired
	private SubmissionService submissionService;

	@PostMapping("/add/{conferenceId}")
	public ResponseEntity<String> addSubmission(@ModelAttribute SubmissionDto submissionDto, @PathVariable Long conferenceId) throws IOException {

		Long result = submissionService.add(submissionDto, conferenceId);

		if (result == -1) {
			return new ResponseEntity<String>("Conference not found !", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<String>("Submission created !", HttpStatus.OK);
		}
	}

	@GetMapping("/{submissionId}")
	public ResponseEntity<Submission> getSubmissionById(@PathVariable Long submissionId) {
		Optional<Submission> submissionOptional = submissionService.findById(submissionId);

		if (submissionOptional.isPresent()) {
			System.out.println("it Works --------------> " +  submissionOptional.get().getId());
			return new ResponseEntity<Submission>(submissionOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Submission>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<Submission>> getAllSubmission() {
		Optional<List<Submission>> submissionListOptional = submissionService.findAllSubmission();

		if (submissionListOptional.isPresent()) {
			return new ResponseEntity<List<Submission>>(submissionListOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Submission>>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/exists/{submissionId}")
	public ResponseEntity<Boolean> existSubmissionById(@PathVariable Long submissionId) {
		boolean isExist = submissionService.existsById(submissionId); 
		return new ResponseEntity<Boolean>(isExist, HttpStatus.OK);
	}

}
