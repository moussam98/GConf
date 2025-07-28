package com.ensao.gi4.controller;

import com.ensao.gi4.dto.ConferenceFirstInfoDto;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.utils.MessageSourceUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("api/v1/users")
record UserConferenceController(ConferenceService conferenceService, MessageSourceUtils messageSourceUtils) {

    @PostMapping("/{userId}/conferences")
    public ResponseEntity<String> addConference(@PathVariable Long userId,
            @RequestBody ConferenceFirstInfoDto conferenceFirstInfoDto) {
        Long result = conferenceService.add(conferenceFirstInfoDto, userId);
        if (result == -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(messageSourceUtils
                    .getMessage(
                            "organizer.conference.conflict",
                            new Object[]{conferenceFirstInfoDto.getName(), conferenceFirstInfoDto.getAcronym()},
                            Locale.ENGLISH));
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
