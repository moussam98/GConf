package com.ensao.gi4.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record ConferenceDto(
		Long id,
		String name,
		String acronym,
		String venue,
		String city,
		String country,
		LocalDate startDate,
		LocalDate endDate,
		String primaryArea,
		String secondaryArea,
		String organizer,
		String phoneNumber,
		String otherInfo,
		List<SubmissionDto> submissions,
		UserDto owner,
		CallForPapersDto callForPapers,
		Instant createdAt,
		Instant updatedAt) {
}

