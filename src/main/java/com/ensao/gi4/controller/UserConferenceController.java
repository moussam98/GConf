package com.ensao.gi4.controller;

import com.ensao.gi4.dto.ConferenceFirstInfoDto;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.service.api.ConferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
record UserConferenceController(ConferenceService conferenceService) {

    @PostMapping("/{userId}/conferences")
    public ResponseEntity<String> addConference(@PathVariable Long userId,
            @RequestBody ConferenceFirstInfoDto conferenceFirstInfoDto) {
        Long result = conferenceService.add(conferenceFirstInfoDto, userId);
        if (result == -1) {
            // TODO: Externalize static message
            return ResponseEntity.badRequest().body(
                    "Unable to add this conference because its name and acronym already exist");
        } else {
            return ResponseEntity.ok(result.toString());
        }
    }

    @GetMapping("/{userId}/conferences")
    public ResponseEntity<Conference> getConferenceByUser(@PathVariable Long userId) {
        return conferenceService.findByUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
