package com.ensao.gi4.controller;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.utils.MessageSourceUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conferences")
record ConferenceController(ConferenceService conferenceService, MessageSourceUtils messageSourceUtils) {

	@GetMapping("/{id}")
	public ResponseEntity<Conference> getConferenceById(@PathVariable Long id){
        return conferenceService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<?> findConference(@RequestParam(required = false) String name,
											@RequestParam(required = false) String acronym) {
		if (name != null) {
			return getConferenceByName(name);
		}
		if (acronym != null) {
			return getConferenceByAcronym(acronym);
		}
		return ResponseEntity.badRequest().build();
	}
	
	private ResponseEntity<Conference> getConferenceByName(String name){
        return conferenceService.findByName(name)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	private ResponseEntity<Conference> getConferenceByAcronym(@PathVariable String acronym){
        return conferenceService.findByAcronym(acronym)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Conference> updateConference(@PathVariable Long id, @RequestBody ConferenceDto conferenceDto) {
        return conferenceService.updateConferenceById(id, conferenceDto)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.noContent().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteConferenceById(@PathVariable Long id){
		conferenceService.deleteById(id);
		return  ResponseEntity.ok(messageSourceUtils.getMessage("organizer.conference.deleted"));
	}

}
