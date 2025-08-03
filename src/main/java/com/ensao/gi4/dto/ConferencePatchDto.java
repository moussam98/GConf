package com.ensao.gi4.dto;

import java.time.LocalDate;

public record ConferencePatchDto(
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
