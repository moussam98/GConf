package com.ensao.gi4.controller;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferencePatchDto;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.utils.MessageSourceUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conferences")
record ConferenceController(ConferenceService conferenceService, MessageSourceUtils messageSourceUtils) {

	@GetMapping("/{id}")
	public ResponseEntity<ConferenceDto> getConferenceById(@PathVariable Long id){
        return conferenceService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ConferenceDto> updateConference(@PathVariable Long id,
														  @RequestBody ConferencePatchDto conferencePatchDto) {
        return conferenceService.updateConferenceById(id, conferencePatchDto)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.noContent().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteConferenceById(@PathVariable Long id){
		conferenceService.deleteById(id);
		return  ResponseEntity.ok(messageSourceUtils.getMessage("organizer.conference.deleted",
				new Object[]{id}));
	}

}
