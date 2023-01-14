package com.ensao.gi4.dto;

import java.util.Set;

import com.ensao.gi4.model.Topic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CallForPapersDto {

	private String startDate; 
	private String endDate;
	private Set<Topic> topics; 
	private String guidelines; 
}
