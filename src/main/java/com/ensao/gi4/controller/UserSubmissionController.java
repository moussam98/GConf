package com.ensao.gi4.controller;

import com.ensao.gi4.dto.SubmissionRequestDto;
import com.ensao.gi4.service.api.SubmissionService;
import com.ensao.gi4.utils.MessageSourceUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
record UserSubmissionController(SubmissionService submissionService, MessageSourceUtils messageSourceUtils) {

    @PostMapping(value = "/{userId}/submissions")
    public ResponseEntity<String> addSubmission(@PathVariable Long userId,
                                                @ModelAttribute SubmissionRequestDto submissionRequestDto) throws IOException {
        if (submissionService.add(submissionRequestDto, userId) == -1) {
            return new ResponseEntity<>(
                        messageSourceUtils.getMessage("organizer.conference.not_found", new Object[]{userId}),
                    HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(messageSourceUtils.getMessage("participant.submission.created",
                    new Object[]{submissionRequestDto.title()}));
        }
    }



}
