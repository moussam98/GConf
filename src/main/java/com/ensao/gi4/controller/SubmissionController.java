package com.ensao.gi4.controller;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.service.api.SubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/submission")
@AllArgsConstructor
public class SubmissionController {

	private final SubmissionService submissionService;

	@PostMapping("/add/{userId}")
	public ResponseEntity<String> addSubmission(@ModelAttribute SubmissionDto submissionDto, @PathVariable Long userId) throws IOException {
		Long result = submissionService.add(submissionDto, userId);

		if (result == -1) {
			return new ResponseEntity<>("Conference not found !", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>("Submission created !", HttpStatus.OK);
		}
	}

	@GetMapping("/{submissionId}")
	public ResponseEntity<Submission> getSubmissionById(@PathVariable Long submissionId) {
		Optional<Submission> submissionOptional = submissionService.findById(submissionId);

        return submissionOptional
				.map(submission -> new ResponseEntity<>(submission, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}

	@GetMapping("/all")
	public ResponseEntity<List<Submission>> getAllSubmission() {
		Optional<List<Submission>> submissionListOptional = submissionService.findAllSubmission();
        return submissionListOptional
				.map(submissions -> new ResponseEntity<>(submissions, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}
	
	@GetMapping("/exists/{submissionId}")
	public ResponseEntity<Boolean> existSubmissionById(@PathVariable Long submissionId) {
		Boolean isExist = submissionService.existsById(submissionId); 
		return new ResponseEntity<>(isExist, HttpStatus.OK);
	}
	
	@PostMapping("/evaluate/{submissionId}")
	public ResponseEntity<Boolean> evaluateSubmission(@PathVariable Long submissionId,
													  @RequestBody EvaluateSubmissionRequest payLoad )  {
		Boolean isValidate = submissionService.evaluateSubmission(submissionId, payLoad.isValidate); 
		return ResponseEntity.ok().body(isValidate); 
	}
	
	@DeleteMapping("/delete/{submissionId}")
	public ResponseEntity<Boolean> deleteSubmissionById(@PathVariable Long submissionId){
		Boolean result = submissionService.deleteById(submissionId);
		return ResponseEntity.ok().body(result); 
	}
	
	public record EvaluateSubmissionRequest(boolean isValidate){	}

}
