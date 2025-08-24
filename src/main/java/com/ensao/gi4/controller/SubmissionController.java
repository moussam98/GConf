package com.ensao.gi4.controller;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.service.api.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/submissions")
record SubmissionController(SubmissionService submissionService) {

	@GetMapping("/{id}")
	public ResponseEntity<SubmissionDto> findById(@PathVariable Long id) {
        return submissionService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}


	@PostMapping("/{id}/evaluations")
	public ResponseEntity<Boolean> evaluate(@PathVariable Long id,
											@RequestBody EvaluateSubmissionRequest payLoad )  {
		return ResponseEntity.ok(submissionService.evaluate(id, payLoad.isValidate));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteById(@PathVariable Long id){
		return ResponseEntity.ok(submissionService.deleteById(id));
	}

	public record EvaluateSubmissionRequest(boolean isValidate){	}

}
