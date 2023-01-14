package com.ensao.gi4.dto.mapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferenceFirstInfoDto;
import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.model.Author;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.model.Keyword;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static Submission toSubmission(SubmissionDto submissionDto) throws IOException {
		Submission submission = submissionMapper(submissionDto);
		return submission;
	}
	
	public static Conference firstInfoToConference(ConferenceFirstInfoDto conferenceFirstInfoDto) {
		Conference conference = new Conference();
		
		conference.setName(conferenceFirstInfoDto.getName().trim());
		conference.setAcronym(conferenceFirstInfoDto.getAcronym().trim());
		
		return conference; 
	}

	public static Conference toConference(ConferenceDto conferenceDto) {
		Conference conference = conferenceMapper(conferenceDto);
		return conference;
	}

	public static User toUser(UserDto userDto) {
		User user = userMapper(userDto);
		return user;
	}

	public static CallForPapers toCallForPapers(CallForPapersDto callForPapersDto) throws JsonMappingException, JsonProcessingException {
		CallForPapers callForPapers = callForPapersMapper(callForPapersDto);
		return callForPapers;
	}

	private static CallForPapers callForPapersMapper(CallForPapersDto callForPapersDto) {
		CallForPapers callForPapers = new CallForPapers();

		callForPapers.setStartDate(getDate(callForPapersDto.getStartDate().trim()));
		callForPapers.setEndDate(getDate(callForPapersDto.getEndDate().trim()));
		callForPapers.setTopics(callForPapersDto.getTopics());
		callForPapers.setGuidelines(callForPapersDto.getGuidelines().trim());
		return callForPapers;
	}
	
	private static User userMapper(UserDto userDto) {
		User user = new User();
	
		user.setFirstname(userDto.getFirstname().trim());
		user.setLastname(userDto.getLastname().trim());
		user.setEmail(userDto.getEmail().trim());
		user.setPassword(userDto.getPassword().trim());
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
		
		Integer year = Integer.valueOf(splitDate[2]);
		Integer month = Integer.valueOf(splitDate[1]);
		Integer dayOfMonth = Integer.valueOf(splitDate[0]);
		
		LocalDate localDate = LocalDate.of(year, month, dayOfMonth);

		return localDate;
	}
	
	private static Submission submissionMapper(SubmissionDto submissionDto)
			throws JsonProcessingException, JsonMappingException, IOException {
		
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

	private static List<Author> getAuthors(SubmissionDto submissionDto)
			throws JsonProcessingException, JsonMappingException {
		List<Author> authorsSubmission = objectMapper.reader().forType(new TypeReference<List<Author>>() {
		}).readValue(submissionDto.getAuthors());
 		return authorsSubmission;
	}

	private static Set<Keyword> getKeywords(SubmissionDto submissionDto)
			throws JsonProcessingException, JsonMappingException { 
		String[] data = submissionDto.getKeywords().split("\n");
		Set<Keyword> keywordSubmission = new HashSet<>(); 
		Arrays.stream(data).forEach((keyword -> keywordSubmission.add(new Keyword(null, keyword.trim()))));
		return keywordSubmission;
	}

}
