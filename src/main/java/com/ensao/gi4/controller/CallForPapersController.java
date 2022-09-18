package com.ensao.gi4.controller;

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

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.service.api.CallForPapersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/v1/cfp")
public class CallForPapersController {

	@Autowired
	private CallForPapersService callForPapersService;

	@PostMapping("/add/{conferenceId}")
	public ResponseEntity<String> addCFP(@ModelAttribute CallForPapersDto callForPapersDto, @PathVariable Long conferenceId) throws JsonMappingException, JsonProcessingException {
		Long result = callForPapersService.add(callForPapersDto, conferenceId);

		if (result == -1) {
			return new ResponseEntity<String>(
					"We cannot add call for papers because the corresponding conference does not exist",
					HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
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
