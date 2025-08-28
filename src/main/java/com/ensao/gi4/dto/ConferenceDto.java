package com.ensao.gi4.dto;

import java.time.Instant;
import java.time.LocalDate;

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
		UserDto owner,
		CallForPapersDto callForPapers,
		Instant createdAt,
		Instant updatedAt) {
}

