package com.ensao.gi4.controller;

import com.ensao.gi4.dto.*;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.service.api.SubmissionService;
import com.ensao.gi4.utils.MessageSourceUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conferences")
record ConferenceController(ConferenceService conferenceService,
							MessageSourceUtils messageSourceUtils,
							SubmissionService submissionService) {

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

	@GetMapping("/{conferenceId}/submissions")
	public ResponseEntity<List<SubmissionDto>> getSubmissionsByConferenceId(@PathVariable Long conferenceId) {
		return ResponseEntity.ok(submissionService.listByConferenceId(conferenceId));
	}

	@GetMapping("/{conferenceId}/submissions/paginated")
	public ResponseEntity<PageResponse<SubmissionDto>> getSubmissionsByConferenceId(@PathVariable Long conferenceId, Pageable pageable) {
		return ResponseEntity.ok(PageResponse.fromPage(submissionService.listByConferenceId(conferenceId, pageable)));
	}

	@PostMapping(value = "/{conferenceId}/submissions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SubmissionDto> addSubmission(@PathVariable Long conferenceId,
													   @ModelAttribute SubmissionRequestDto submissionRequestDto){
		return ResponseEntity.ok(submissionService.createByConferenceId(conferenceId, submissionRequestDto));
	}

	@GetMapping("/{conferenceId}/submissions/statistics")
	public ResponseEntity<SubmissionStatisticsDto> getSubmissionStatisticsByConferenceId(@PathVariable Long conferenceId) {
		return ResponseEntity.ok(submissionService.getSubmissionStatisticsByConferenceId(conferenceId));
	}

}
