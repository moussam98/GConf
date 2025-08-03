package com.ensao.gi4.controller;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferenceRequestDto;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.service.exception.ConferenceNotFoundException;
import com.ensao.gi4.utils.MessageSourceUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
record UserConferenceController(ConferenceService conferenceService, MessageSourceUtils messageSourceUtils) {

    @PostMapping("/{userId}/conferences")
    public ResponseEntity<ConferenceDto> addConference(@PathVariable Long userId,
                                                       @RequestBody ConferenceRequestDto conferenceRequestDto) {
            return ResponseEntity.ok(conferenceService.add(conferenceRequestDto, userId));
    }

    @GetMapping("/{ownerId}/conferences")
    public ResponseEntity<ConferenceDto> getConferenceByOwnerId(@PathVariable Long ownerId) {
        return conferenceService.findByOwnerId(ownerId)
                .map(ResponseEntity::ok)
                .orElseThrow( () ->
                        new ConferenceNotFoundException(messageSourceUtils.getMessage(
                        "organizer.conference.not_found", new Object[]{ownerId})));
    }

}
