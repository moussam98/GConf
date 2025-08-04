package com.ensao.gi4.controller;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.dto.CallForPapersRequestDto;
import com.ensao.gi4.service.api.CallForPapersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conferences")
record CallForPapersController(CallForPapersService callForPapersService) {

	@PostMapping("/{id}/cfp")
	public ResponseEntity<CallForPapersDto> addCFP(
			@PathVariable Long id,
			@RequestBody CallForPapersRequestDto callForPapersRequestDto) {
        return  callForPapersService.add(callForPapersRequestDto, id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.badRequest().build());
	}

	@GetMapping("/{id}/cfp")
	public ResponseEntity<CallForPapersDto> getCFPByConferenceId(@PathVariable Long id) {
        return callForPapersService.findByConferenceId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
}
