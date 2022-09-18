package com.ensao.gi4.dto.mapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.model.Author;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.model.Keyword;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.model.Topic;
import com.ensao.gi4.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static Submission toSubmission(SubmissionDto submissionDto) throws IOException {
		Submission submission = new Submission();

		submission.setTitle(submissionDto.getTitle());
		submission.setDescription(submissionDto.getDescription());
		submission.setKeywords(getKeywords(submissionDto));
		submission.setAuthors(getAuthors(submissionDto));
		submission.setDocument(generateDocument(submissionDto));

		return submission;
	}

	public static Conference toConference(ConferenceDto conferenceDto) {
		Conference conference = new Conference();

		conference.setName(conferenceDto.getName());
		conference.setAcronym(conferenceDto.getAcronym());
		conference.setVenue(conferenceDto.getVenue());
		conference.setCity(conferenceDto.getCity());
		conference.setCountry(conferenceDto.getCity());
		conference.setFirstDay(setDate(conferenceDto.getFirstDay()));
		conference.setLastDay(setDate(conferenceDto.getLastDay()));
		conference.setPrimaryArea(conferenceDto.getPrimaryArea());
		conference.setSecondaryArea(conferenceDto.getSecondaryArea());
		conference.setOrganizer(conferenceDto.getOrganizer());
		conference.setPhoneNumber(conferenceDto.getPhoneNumber());
		conference.setOtherInfo(conferenceDto.getOtherInfo());

		return conference;
	}

	public static User toUser(UserDto userDto) {
		User user = new User();

		user.setFirstname(userDto.getFirstname());
		user.setLastname(userDto.getLastname());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());

		return user;
	}

	public static CallForPapers toCallForPapers(CallForPapersDto callForPapersDto) throws JsonMappingException, JsonProcessingException {
		CallForPapers callForPapers = new CallForPapers();

		callForPapers.setStartDate(setDate(callForPapersDto.getStartDate()));
		callForPapers.setEndDate(setDate(callForPapersDto.getEndDate()));
		callForPapers.setTopics(getTopics(callForPapersDto));
		callForPapers.setGuidelines(callForPapersDto.getGuidelines());

		return callForPapers;
	}

	private static Set<Topic> getTopics(CallForPapersDto callForPapersDto)
			throws JsonProcessingException, JsonMappingException {
		Set<Topic> callForPapersTopics = objectMapper.reader().forType(new TypeReference<Set<Topic>>() {
		}).readValue(callForPapersDto.getTopics());

		return callForPapersTopics;
	}

	private static LocalDate setDate(String date) {
		String[] splitDate = date.split("/");

		LocalDate localDate = LocalDate.of(Integer.valueOf(splitDate[0]), Integer.valueOf(splitDate[1]),
				Integer.valueOf(splitDate[2]));

		return localDate;
	}

	private static Document generateDocument(SubmissionDto submissionDto) throws IOException {
		Document document = new Document();
		document.setFilename(submissionDto.getDocument().getOriginalFilename());
		document.setFileType(submissionDto.getDocument().getContentType());
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
		Set<Keyword> keywordSubmission = objectMapper.reader().forType(new TypeReference<Set<Keyword>>() {
		}).readValue(submissionDto.getKeywords());
		return keywordSubmission;
	}

}
