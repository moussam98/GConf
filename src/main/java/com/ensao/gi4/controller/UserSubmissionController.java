package com.ensao.gi4.controller;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.service.api.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
record UserSubmissionController(SubmissionService submissionService) {

    @PostMapping("/{userId}/submissions")
    public ResponseEntity<String> addSubmission(@ModelAttribute SubmissionDto submissionDto,
                                                @PathVariable Long userId) throws IOException {
        if (submissionService.add(submissionDto, userId) == -1) {
            return new ResponseEntity<>("Conference not found !", HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok("Submission created !");
        }
    }



}
