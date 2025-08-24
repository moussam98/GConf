package com.ensao.gi4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Getter
public class CallForPapersRequestDto {

	private LocalDate startDate;
	private LocalDate endDate;
	private Set<String> topics;
	private String guidelines; 
}
