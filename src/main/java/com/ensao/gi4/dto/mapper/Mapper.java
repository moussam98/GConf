package com.ensao.gi4.dto.mapper;

import com.ensao.gi4.dto.*;
import com.ensao.gi4.model.*;
import com.ensao.gi4.projection.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

// TODO: find a new way to map a bean DTO. Use a library
// TODO: Handle null safety, explore about Jspecify
public class Mapper {

	public static Submission toSubmission(SubmissionRequestDto submissionRequestDto) throws IOException {
        return submissionMapper(submissionRequestDto);
	}


	public static SubmissionDto toSubmissionDto(Submission submission){
        return new SubmissionDto(
				submission.getId(),
				submission.getTitle(),
				submission.getDescription(),
				submission.getKeywords(),
				Mapper.toDocumentMetadataDto(submission.getDocument()),
				submission.getAuthors().stream().map(Mapper::toAuthorDto).toList(),
				submission.getIsEvaluate(),
				submission.getIsValidate()
		);
	}

	public static SubmissionDto toSubmissionDto(SubmissionProjection submissionProjection){
        return new SubmissionDto(
				submissionProjection.getId(),
				submissionProjection.getTitle(),
				submissionProjection.getDescription(),
				submissionProjection.getKeywords(),
				Mapper.toDocumentMetadataDto(submissionProjection.getDocument()),
				submissionProjection.getAuthors().stream().map(Mapper::toAuthorDto).toList(),
				submissionProjection.getIsEvaluate(),
				submissionProjection.getIsValidate()
		);
	}

	public static DocumentMetadataDto toDocumentMetadataDto(DocumentProjection documentProjection){
		return new DocumentMetadataDto(
				documentProjection.getId(),
				documentProjection.getFilename(),
				documentProjection.getFileType(),
				documentProjection.getSizeInBytes()
		);
	}

	public static DocumentMetadataDto toDocumentMetadataDto(Document document){
		return new DocumentMetadataDto(
				document.getId(),
				document.getFilename(),
				document.getFileType(),
				document.getSizeInBytes()
		);
	}

	private static AuthorDto toAuthorDto(Author author) {
		return new AuthorDto(
				author.getFirstName(),
				author.getLastName(),
				author.getEmail(),
				author.getCountry(),
				author.getOrganization());
	}

	private static AuthorDto toAuthorDto(AuthorProjection authorProjection) {
		return new AuthorDto(
				authorProjection.getFirstName(),
				authorProjection.getLastName(),
				authorProjection.getEmail(),
				authorProjection.getCountry(),
				authorProjection.getOrganization());
	}

	private static Author toAuthor(AuthorDto authorDto) {
		Author author = new Author();
		author.setFirstName(authorDto.firstName());
		author.setLastName(authorDto.lastName());
		author.setEmail(authorDto.email());
		author.setCountry(authorDto.country());
		author.setOrganization(authorDto.organization());

		return  author;
	}


	public static Conference toConference(ConferenceRequestDto conferenceRequestDto) {
        return conferenceMapper(conferenceRequestDto);
	}

	public static ConferenceDto toConferenceDto(ConferenceProjection conferenceProjection){
		return new ConferenceDto(
				conferenceProjection.getId(),
				conferenceProjection.getName(),
				conferenceProjection.getAcronym(),
				conferenceProjection.getVenue(),
				conferenceProjection.getCity(),
				conferenceProjection.getCountry(),
				conferenceProjection.getStartDate(),
				conferenceProjection.getEndDate(),
				conferenceProjection.getPrimaryArea(),
				conferenceProjection.getSecondaryArea(),
				conferenceProjection.getOrganizer(),
				conferenceProjection.getPhoneNumber(),
				conferenceProjection.getOtherInfo(),
				Collections.emptyList(),
				Mapper.toUserDto(conferenceProjection.getOwner()),
				Mapper.toCallForPapersDto(conferenceProjection.getCallForPapers()),
				conferenceProjection.getCreatedAt(),
				conferenceProjection.getUpdatedAt()
		);
	}

	public static ConferenceDto toConferenceDto(Conference conference) {
        return new ConferenceDto(
				conference.getId(),
				conference.getName(),
				conference.getAcronym(),
				conference.getVenue(),
				conference.getCity(),
				conference.getCountry(),
				conference.getStartDate(),
				conference.getEndDate(),
				conference.getPrimaryArea(),
				conference.getSecondaryArea(),
				conference.getOrganizer(),
				conference.getPhoneNumber(),
				conference.getOtherInfo(),
				mapToSubmissionDto(conference.getSubmissions()),
				Mapper.toUserDto(conference.getOwner()),
				Mapper.toCallForPapersDto(conference.getCallForPapers()),
				conference.getCreatedAt(),
				conference.getUpdatedAt()
		);
	}

	private static List<SubmissionDto> mapToSubmissionDto(List<Submission> submissions) {
		if (submissions == null || submissions.isEmpty()) {
			return Collections.emptyList();
		} else {
			return submissions.stream()
					.map(Mapper::toSubmissionDto).toList();
		}
	}

	public static User toUser(UserRequestDto userDto) {
        return userMapper(userDto);
	}

	public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(),
				user.getEmail(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt());
	}

	public static UserDto toUserDto(UserProjection userProjection) {
        return new UserDto(
				userProjection.getId(),
				userProjection.getFirstName(), userProjection.getLastName(),
				userProjection.getEmail(), userProjection.getRole(),
				userProjection.getCreatedAt(), userProjection.getUpdatedAt());
	}

	public static User toUser(UserDto userDto) {
		User user = new User();
		user.setId(userDto.id());
		user.setFirstName(userDto.firstName().trim());
		user.setLastName(userDto.lastName().trim());
		user.setEmail(userDto.email().trim());
		user.setRole(userDto.role());
		user.setCreatedAt(userDto.createdAt());
		user.setUpdatedAt(userDto.updatedAt());
		return user;
	}

	public static CallForPapers toCallForPapers(CallForPapersRequestDto callForPapersRequestDto) {
        return callForPapersMapper(callForPapersRequestDto);
	}

	public static CallForPapersDto toCallForPapersDto(CallForPapersProjection callForPapersProjection) {
		if(callForPapersProjection == null){
			return null;
		}
        return new CallForPapersDto(
				callForPapersProjection.getId(),
				callForPapersProjection.getStartDate(),
				callForPapersProjection.getEndDate(),
				callForPapersProjection.getTopics(),
				callForPapersProjection.getGuidelines()
		);
	}

	public static CallForPapersDto toCallForPapersDto(CallForPapers callForPapers) {
		if(callForPapers == null){
			return null;
		}
        return new CallForPapersDto(
				callForPapers.getId(),
				callForPapers.getStartDate(),
				callForPapers.getEndDate(),
				callForPapers.getTopics(),
				callForPapers.getGuidelines()
		);
	}

	private static CallForPapers callForPapersMapper(CallForPapersRequestDto callForPapersRequestDto) {
		CallForPapers callForPapers = new CallForPapers();

		callForPapers.setStartDate(callForPapersRequestDto.getStartDate());
		callForPapers.setEndDate(callForPapersRequestDto.getEndDate());
		callForPapers.setTopics(callForPapersRequestDto.getTopics());
		callForPapers.setGuidelines(callForPapersRequestDto.getGuidelines().trim());
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
	
	private static Conference conferenceMapper(ConferenceRequestDto conferenceRequestDto) {
		Conference conference = new Conference();
		conference.setName(conferenceRequestDto.name());
		conference.setAcronym(conferenceRequestDto.acronym());
		conference.setVenue(conferenceRequestDto.venue());
		conference.setCity(conferenceRequestDto.city());
		conference.setCountry(conferenceRequestDto.country());
		conference.setStartDate(conferenceRequestDto.startDate());
		conference.setEndDate(conferenceRequestDto.endDate());
		conference.setPrimaryArea(conferenceRequestDto.primaryArea());
		conference.setSecondaryArea(conferenceRequestDto.secondaryArea());
		conference.setOrganizer(conferenceRequestDto.organizer());
		conference.setPhoneNumber(conferenceRequestDto.phoneNumber());
		conference.setOtherInfo(conferenceRequestDto.otherInfo());

		return conference;
	}

	private static Submission submissionMapper(SubmissionRequestDto submissionRequestDto) throws IOException {
		Submission submission = new Submission();
		submission.setTitle(submissionRequestDto.title());
		submission.setDescription(submissionRequestDto.description());
		submission.setKeywords(submissionRequestDto.keywords());
		List<Author> authors = submissionRequestDto.authors() != null ?
				submissionRequestDto.authors().stream().map(Mapper::toAuthor).toList()
				: Collections.emptyList();
		submission.setAuthors(authors);
		submission.setDocument(generateDocument(submissionRequestDto.document()));

		return submission;
	}

	private static Document generateDocument(MultipartFile file) throws IOException {
		Document document = new Document();
		document.setFilename(file.getOriginalFilename());
		document.setFileType(file.getContentType());
		document.setData(file.getBytes());
		document.setSizeInBytes(file.getSize());
		return document;
	}

}
