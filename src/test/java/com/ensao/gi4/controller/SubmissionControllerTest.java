package com.ensao.gi4.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Keyword;
import com.ensao.gi4.model.Person;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.service.api.SubmissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebMvcTest(SubmissionController.class)
public class SubmissionControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private SubmissionService submissionService;
	private String baseUrl = "/api/v1/submission";
	private Conference conference;
	private Person person1, person2, person3, person4;
	private Keyword keyword1, keyword2, keyword3, keyword4;
	private MockMultipartFile document, submissionMultipartFile;
	private SubmissionDto submissionDto;
	private Submission submission;

	@BeforeEach
	void setUp() throws IOException {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conference.setId(1L);

//		person1 = new Person(null, "Ali1", "Moussa", "ali@gmail.com", "Morocco", "ENSAO", null);
//		person2 = new Person(null, "Ali2", "Moussa", "ali@gmail.com", "Morocco", "ENSAO", null);
//		person3 = new Person(null, "Ali3", "Moussa", "ali@gmail.com", "Morocco", "ENSAO", null);
//		person4 = new Person(null, "Ali4", "Moussa", "ali@gmail.com", "Morocco", "ENSAO", null);

		List<Person> authors = new ArrayList<>();
		authors.add(person1);
		authors.add(person2);
		authors.add(person3);
		authors.add(person4);

		keyword1 = new Keyword(null, "Artificial Intelligence");
		keyword2 = new Keyword(null, "Internet Of Things");
		keyword3 = new Keyword(null, "Block chain");
		keyword4 = new Keyword(null, "Technologie");

		List<Keyword> keywords = new ArrayList<>();
		keywords.add(keyword1);
		keywords.add(keyword2);
		keywords.add(keyword3);
		keywords.add(keyword4);

		document = new MockMultipartFile("Test", "Test.pdf", MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

//		submissionDto = new SubmissionDto();
//		submissionDto.setTitle("Subject");
//		submissionDto.setDescription("Description of subject");
//		submissionDto.setConferenceId(conference.getId());
//		submissionDto.setDocument(document);
		//submissionDto.setKeywords(keywords);
		//submissionDto.setAuthors(authors);

		submission = Mapper.toSubmission(submissionDto);
		submission.setDocument(null);
		submission.setId(2l);
		
		//mu
	}

	@Test
	void shouldAddSubmission() throws Exception {
		// given
		Long conferenceId = conference.getId();
		//submissionMultipartFile = new MockMultipartFile("json", "application/json", baseUrl, null)

		// when
		//when(submissionService.add(submission, conferenceId)).thenReturn(submission.getId());
		ArgumentCaptor<Submission> submissionArgumentCaptor = ArgumentCaptor.forClass(Submission.class);
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		MvcResult expectedResponse = mockMvc
				.perform(post(baseUrl))
				.andExpect(status().isOk()).andReturn();

		// then
		//verify(submissionService).add(submissionArgumentCaptor.capture(), conferenceIdArgumentCaptor.capture());
		assertThat(submissionArgumentCaptor.getValue()).isEqualTo(submission);
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conferenceId);
		assertThat(getResponseContent(expectedResponse)).isEqualTo(submission.getId().toString());
	}

	private String getResponseContent(MvcResult expectedResponse) throws UnsupportedEncodingException {
		return expectedResponse.getResponse().getContentAsString();
	}

}
