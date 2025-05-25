package com.ensao.gi4.controller;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferenceFirstInfoDto;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.service.api.ConferenceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/conference")
@AllArgsConstructor
public class ConferenceController {

	private final ConferenceService conferenceService;
 
	@PostMapping("/add/{userId}")
	public ResponseEntity<String> addConference(@RequestBody ConferenceFirstInfoDto conferenceFirstInfoDto, @PathVariable Long userId) {
		
		Long result = conferenceService.add(conferenceFirstInfoDto, userId);

		if (result == -1) {
			return new ResponseEntity<>("Unable to add this conference because its name and acronym already exist", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(result.toString(), HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/{conferenceId}")
	public ResponseEntity<Conference> getConferenceById(@PathVariable Long conferenceId){
		Optional<Conference> conferenceOptional = conferenceService.findById(conferenceId);

        return conferenceOptional
				.map(conference -> ResponseEntity.ok().body(conference))
				.orElseGet(() -> ResponseEntity.noContent().build());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Conference> getConferenceByName(@PathVariable String name){
		Optional<Conference> conferenceOptional = conferenceService.findByName(name);

        return conferenceOptional
				.map(conference -> new ResponseEntity<>(conference, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}
	
	@GetMapping("/acronym/{acronym}")
	public ResponseEntity<Conference> getConferenceByAcronym(@PathVariable String acronym){
		Optional<Conference> conferenceOptional = conferenceService.findByAcronym(acronym);

        return conferenceOptional
				.map(conference -> new ResponseEntity<>(conference, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<Conference> getConferenceByUser(@PathVariable Long userId) {
		
		Optional<Conference> conferenceOptional = conferenceService.findByUser(userId);
        return conferenceOptional
				.map(conference -> new ResponseEntity<>(conference, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}
	
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> existsConferenceById(@PathVariable Long id){
		boolean exists = conferenceService.existsById(id); 
		
		return new ResponseEntity<>(exists, HttpStatus.OK);
	
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Conference> updateConference(@PathVariable Long id, @RequestBody ConferenceDto conferenceDto) {
		 Optional<Conference> conferenceOptional = conferenceService.updateConferenceById(id, conferenceDto);
        return conferenceOptional.map(conference -> ResponseEntity.ok().body(conference)).orElseGet(() -> ResponseEntity.noContent().build());
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteConferenceById(@PathVariable("id") Long id){
		
		conferenceService.deleteById(id);
		
		return  ResponseEntity.ok().body("Conference deleted successfully");
	}

}
