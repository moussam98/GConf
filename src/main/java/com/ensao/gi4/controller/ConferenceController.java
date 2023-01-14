package com.ensao.gi4.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferenceFirstInfoDto;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.service.api.ConferenceService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/conference")
@AllArgsConstructor
public class ConferenceController {

	private final ConferenceService conferenceService;
 
	@PostMapping("/add/{userId}")
	public ResponseEntity<String> addConference(@RequestBody ConferenceFirstInfoDto conferenceFirstInfoDto, @PathVariable Long userId) {
		
		Long result = conferenceService.add(conferenceFirstInfoDto, userId);

		if (result == -1) {
			return new ResponseEntity<String>("Unable to add this conference because its name and acronym already exist" ,HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/{conferenceId}")
	public ResponseEntity<Conference> getConferenceById(@PathVariable Long conferenceId){
		Optional<Conference> conferenceOptional = conferenceService.findById(conferenceId); 
		
		if (conferenceOptional.isPresent()) {
			return ResponseEntity.ok().body(conferenceOptional.get()); 
		}
		else {
			return ResponseEntity.noContent().build(); 
		}
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Conference> getConferenceByName(@PathVariable String name){
		Optional<Conference> conferenceOptional = conferenceService.findByName(name); 
		
		if (conferenceOptional.isPresent()) {
			return new ResponseEntity<Conference>(conferenceOptional.get(), HttpStatus.OK); 
		}
		else {
			return new ResponseEntity<Conference>(HttpStatus.NO_CONTENT); 
		}
	}
	
	@GetMapping("/acronym/{acronym}")
	public ResponseEntity<Conference> getConferenceByAcronym(@PathVariable String acronym){
		Optional<Conference> conferenceOptional = conferenceService.findByAcronym(acronym); 
		
		if (conferenceOptional.isPresent()) {
			return new ResponseEntity<Conference>(conferenceOptional.get(), HttpStatus.OK); 
		}
		else {
			return new ResponseEntity<Conference>(HttpStatus.NO_CONTENT); 
		}
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<Conference> getConferenceByUser(@PathVariable Long userId) {
		
		Optional<Conference> conferenceOptional = conferenceService.findByUser(userId); 
		if (conferenceOptional.isPresent()) {
			return new ResponseEntity<Conference>(conferenceOptional.get(), HttpStatus.OK); 
		}
		else {
			return new ResponseEntity<Conference>(HttpStatus.NO_CONTENT); 
		}
	}
	
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> existsConferenceById(@PathVariable Long id){
		boolean exists = conferenceService.existsById(id); 
		
		return new ResponseEntity<Boolean>(exists, HttpStatus.OK); 
	
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Conference> updateConference(@PathVariable Long id, @RequestBody ConferenceDto conferenceDto) {
		 Optional<Conference> conferenceOptional = conferenceService.updateConferenceById(id, conferenceDto);
		 if (conferenceOptional.isPresent()) {
			return ResponseEntity.ok().body(conferenceOptional.get());
		}else {
			return ResponseEntity.noContent().build(); 
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteConferenceById(@PathVariable("id") Long id){
		
		conferenceService.deleteById(id);
		
		return  ResponseEntity.ok().body("Conference deleted successfully");
	}

}
