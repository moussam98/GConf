package com.ensao.gi4.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ConferenceRequestDto(
		@NotBlank String name,
		@NotBlank String acronym,
		String venue,
		String city,
		String country,
		LocalDate startDate,
		LocalDate endDate,
		String primaryArea,
		String secondaryArea,
		String organizer,
		String phoneNumber,
		String otherInfo) {
}
