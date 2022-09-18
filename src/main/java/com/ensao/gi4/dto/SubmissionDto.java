package com.ensao.gi4.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubmissionDto{
	
	private final String title; 
	private final String description;
	private final MultipartFile document;
	private final String keywords;
	private final String authors; 

}
