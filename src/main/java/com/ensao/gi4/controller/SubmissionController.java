package com.ensao.gi4.controller;

import com.ensao.gi4.model.Submission;
import com.ensao.gi4.service.api.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/submissions")
record SubmissionController(SubmissionService submissionService) {

	@GetMapping("/{id}")
	public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        return submissionService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<Submission>> getAllSubmission() {
        return submissionService.findAllSubmission()
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}
	
	@PostMapping("/{id}/evaluations")
	public ResponseEntity<Boolean> evaluateSubmission(@PathVariable Long id,
													  @RequestBody EvaluateSubmissionRequest payLoad )  {
		return ResponseEntity.ok(submissionService.evaluateSubmission(id, payLoad.isValidate));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteSubmissionById(@PathVariable Long id){
		return ResponseEntity.ok(submissionService.deleteById(id));
	}
	
	public record EvaluateSubmissionRequest(boolean isValidate){	}

}
