package com.ensao.gi4.dto.mapper;

import com.ensao.gi4.dto.*;
import com.ensao.gi4.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: find a new way to map a bean DTO. Use a library
public class Mapper {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static Submission toSubmission(SubmissionDto submissionDto) throws IOException {
        return submissionMapper(submissionDto);
	}
	
	public static Conference firstInfoToConference(ConferenceFirstInfoDto conferenceFirstInfoDto) {
		Conference conference = new Conference();
		
		conference.setName(conferenceFirstInfoDto.getName().trim());
		conference.setAcronym(conferenceFirstInfoDto.getAcronym().trim());
		
		return conference; 
	}

	public static Conference toConference(ConferenceDto conferenceDto) {
        return conferenceMapper(conferenceDto);
	}

	public static User toUser(UserRequestDto userDto) {
        return userMapper(userDto);
	}

	public static UserResponseDto toUserDto(User user) {
		var conferenceId= user.getConference() != null ? user.getConference().getId():null;
        return new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(),
				user.getEmail(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt(),
				conferenceId);
	}

	public static User toUser(UserResponseDto userResponseDto) {
		User user = new User();
		user.setId(userResponseDto.id());
		user.setFirstName(userResponseDto.firstName().trim());
		user.setLastName(userResponseDto.lastName().trim());
		user.setEmail(userResponseDto.email().trim());
		user.setRole(userResponseDto.role());
		return user;
	}

	public static CallForPapers toCallForPapers(CallForPapersDto callForPapersDto) {
        return callForPapersMapper(callForPapersDto);
	}

	private static CallForPapers callForPapersMapper(CallForPapersDto callForPapersDto) {
		CallForPapers callForPapers = new CallForPapers();

		callForPapers.setStartDate(getDate(callForPapersDto.getStartDate().trim()));
		callForPapers.setEndDate(getDate(callForPapersDto.getEndDate().trim()));
		callForPapers.setTopics(callForPapersDto.getTopics());
		callForPapers.setGuidelines(callForPapersDto.getGuidelines().trim());
		return callForPapers;
	}
	
	private static User userMapper(UserRequestDto userDto) {
		User user = new User();
		user.setFirstName(userDto.firstName().trim());
		user.setLastName(userDto.lastName().trim());
		user.setEmail(userDto.email().trim());
		user.setPassword(userDto.password().trim());
		return user;
	}
	
	private static Conference conferenceMapper(ConferenceDto conferenceDto) {
		Conference conference = new Conference();

		conference.setName(conferenceDto.getName().trim());
		conference.setAcronym(conferenceDto.getAcronym().trim());
		conference.setVenue(conferenceDto.getVenue().trim());
		conference.setCity(conferenceDto.getCity().trim());
		conference.setCountry(conferenceDto.getCountry().trim());
		conference.setFirstDay(getDate(conferenceDto.getFirstDay().trim()));
		conference.setLastDay(getDate(conferenceDto.getLastDay().trim()));
		conference.setPrimaryArea(conferenceDto.getPrimaryArea().trim());
		conference.setSecondaryArea(conferenceDto.getSecondaryArea().trim());
		conference.setOrganizer(conferenceDto.getOrganizer().trim());
		conference.setPhoneNumber(conferenceDto.getPhoneNumber().trim());
		conference.setOtherInfo(conferenceDto.getOtherInfo().trim());
		return conference;
	}

	private static LocalDate getDate(String date) {
		String[] splitDate = date.split("/");
		
		int year = Integer.parseInt(splitDate[2]);
		int month = Integer.parseInt(splitDate[1]);
		int dayOfMonth = Integer.parseInt(splitDate[0]);

        return LocalDate.of(year, month, dayOfMonth);
	}
	
	private static Submission submissionMapper(SubmissionDto submissionDto) throws IOException {
		
		Submission submission = new Submission();
		
		submission.setTitle(submissionDto.getTitle().trim());
		submission.setDescription(submissionDto.getDescription().trim());
		submission.setKeywords(getKeywords(submissionDto));
		submission.setAuthors(getAuthors(submissionDto));
		submission.setDocument(generateDocument(submissionDto));
		return submission;
	}

	private static Document generateDocument(SubmissionDto submissionDto) throws IOException {
		Document document = new Document();
		document.setFilename(submissionDto.getDocument().getOriginalFilename().trim());
		document.setFileType(submissionDto.getDocument().getContentType().trim());
		document.setData(submissionDto.getDocument().getBytes());
		return document;
	}

	private static List<Author> getAuthors(SubmissionDto submissionDto) throws JsonProcessingException {
        return objectMapper.reader().forType(new TypeReference<List<Author>>() {
        }).readValue(submissionDto.getAuthors());
	}

	private static Set<Keyword> getKeywords(SubmissionDto submissionDto) {
		String[] data = submissionDto.getKeywords().split("\n");
		Set<Keyword> keywordSubmission = new HashSet<>(); 
		Arrays.stream(data).forEach((keyword -> keywordSubmission.add(new Keyword(null, keyword.trim()))));
		return keywordSubmission;
	}

}
