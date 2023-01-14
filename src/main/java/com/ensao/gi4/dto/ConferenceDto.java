package com.ensao.gi4.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConferenceDto {

	private String name;
	private String acronym;
	private String venue;
	private String city;
	private String country;
	private String firstDay;
	private String lastDay;
	private String primaryArea;
	private String secondaryArea;
	private String organizer;
	private String phoneNumber;
	private String otherInfo;

}
