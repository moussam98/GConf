package com.ensao.gi4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CallForPapersDto {

	private final String startDate; 
	private final String endDate;
	private final String topics; 
	private final String guidelines; 
}
