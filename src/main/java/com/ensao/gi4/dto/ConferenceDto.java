package com.ensao.gi4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConferenceDto {

	private final String name;
	private final String acronym;
	private final String venue;
	private final String city;
	private final String country;
	private final String firstDay;
	private final String lastDay;
	private final String primaryArea;
	private final String secondaryArea;
	private final String organizer;
	private final String phoneNumber;
	private final String otherInfo;

}
