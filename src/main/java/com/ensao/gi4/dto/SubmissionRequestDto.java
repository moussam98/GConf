package com.ensao.gi4.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public record SubmissionRequestDto(
	String title,
	String description,
	MultipartFile document,
	Set<String> keywords,
	List<AuthorDto> authors){

}