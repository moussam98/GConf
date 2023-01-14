package com.ensao.gi4.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubmissionDto{
	
	private String title; 
	private String description;
	private MultipartFile document;
	private String keywords;
	private String authors; 

}
