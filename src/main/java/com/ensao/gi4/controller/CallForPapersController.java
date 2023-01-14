package com.ensao.gi4.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.service.api.CallForPapersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/cfp")
@AllArgsConstructor
public class CallForPapersController {

	private final CallForPapersService callForPapersService;

	@PostMapping("/add/{conferenceId}")
	public ResponseEntity<CallForPapers> addCFP(@RequestBody CallForPapersDto callForPapersDto, @PathVariable Long conferenceId) throws JsonMappingException, JsonProcessingException {
		 Optional<CallForPapers> callForPapersOptional = callForPapersService.add(callForPapersDto, conferenceId);

		if (callForPapersOptional.isPresent()) {
			return new ResponseEntity<CallForPapers>(callForPapersOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<CallForPapers>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{conferenceId}")
	public ResponseEntity<CallForPapers> getCFPByConferenceId(@PathVariable Long conferenceId) {
		Optional<CallForPapers> cfpOptional = callForPapersService.findByConferenceId(conferenceId);

		if (cfpOptional.isPresent()) {
			return new ResponseEntity<CallForPapers>(cfpOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<CallForPapers>(HttpStatus.NO_CONTENT);
		}
	}
	
	

	@GetMapping("/exists/{conferenceId}")
	public ResponseEntity<Boolean> existCFPByConferenceId(@PathVariable Long conferenceId) {
		boolean isExist = callForPapersService.existsByConferenceId(conferenceId);

		return new ResponseEntity<Boolean>(isExist, HttpStatus.OK);
	}
}
