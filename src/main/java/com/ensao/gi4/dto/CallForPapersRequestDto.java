package com.ensao.gi4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class CallForPapersRequestDto {

	private String startDate; 
	private String endDate;
	private Set<String> topics;
	private String guidelines; 
}
