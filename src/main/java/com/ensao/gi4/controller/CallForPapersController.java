package com.ensao.gi4.controller;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.service.api.CallForPapersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cfp")
@AllArgsConstructor
public class CallForPapersController {

	private final CallForPapersService callForPapersService;

	@PostMapping("/add/{conferenceId}")
	public ResponseEntity<CallForPapers> addCFP(@RequestBody CallForPapersDto callForPapersDto, @PathVariable Long conferenceId) throws JsonProcessingException {
		 Optional<CallForPapers> callForPapersOptional = callForPapersService.add(callForPapersDto, conferenceId);

        return callForPapersOptional
				.map(callForPapers -> new ResponseEntity<>(callForPapers, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}

	@GetMapping("/{conferenceId}")
	public ResponseEntity<CallForPapers> getCFPByConferenceId(@PathVariable Long conferenceId) {
		Optional<CallForPapers> cfpOptional = callForPapersService.findByConferenceId(conferenceId);

        return cfpOptional
				.map(callForPapers -> new ResponseEntity<>(callForPapers, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}
	
	

	@GetMapping("/exists/{conferenceId}")
	public ResponseEntity<Boolean> existCFPByConferenceId(@PathVariable Long conferenceId) {
		boolean isExist = callForPapersService.existsByConferenceId(conferenceId);

		return new ResponseEntity<>(isExist, HttpStatus.OK);
	}
}
