package com.ensao.gi4.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Topic;
import com.ensao.gi4.service.api.CallForPapersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CallForPapersController.class)
public class CallForPapersControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private CallForPapersService callForPapersService;
	private String baseUrl = "/api/v1/cfp";
	private Conference conference;
	private CallForPapers callForPapers;
	private Long conferenceId;

	@BeforeEach
	void setUp() {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conference.setId(1l);
		conferenceId = conference.getId();

//		Topic topic1 = new Topic(null, "Medical", null);
//		Topic topic2 = new Topic(null, "Agricultural", null);
//		Topic topic3 = new Topic(null, "Automotive", null);
//		Topic topic4 = new Topic(null, "Education", null);
		Topic topic1 = new Topic(null, "Medical");
		Topic topic2 = new Topic(null, "Agricultural");
		Topic topic3 = new Topic(null, "Automotive");
		Topic topic4 = new Topic(null, "Education");

		Set<Topic> topics = new HashSet<>();
		topics.add(topic1);
		topics.add(topic2);
		topics.add(topic3);
		topics.add(topic4);

		callForPapers = new CallForPapers();
		callForPapers.setStartDate(LocalDate.now());
		callForPapers.setEndDate(LocalDate.of(2022, 8, 30));
		callForPapers.setGuidelines("Guidelines instruction");
		callForPapers.setTopics(topics);
		callForPapers.setId(2l);
	}

	@Test
	void shouldAddCFP() throws JsonProcessingException, Exception {
		// when
		//when(callForPapersService.add(callForPapers, conferenceId)).thenReturn(callForPapers.getId());
		ArgumentCaptor<CallForPapers> cfpArgumentCaptor = ArgumentCaptor.forClass(CallForPapers.class);
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		MvcResult expectedResponse = mockMvc
				.perform(post(baseUrl + "/add/" + conferenceId).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(callForPapers)))
				.andExpect(status().isOk()).andReturn();

		// then
		//verify(callForPapersService).add(cfpArgumentCaptor.capture(), conferenceIdArgumentCaptor.capture());
		assertThat(cfpArgumentCaptor.getValue()).isEqualTo(callForPapers);
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conferenceId);
		assertThat(getResponseContent(expectedResponse)).isEqualTo(callForPapers.getId().toString());

	}

	@Test
	void shouldNotAddCFP() throws JsonProcessingException, Exception {
		// when
		//when(callForPapersService.add(callForPapers, conferenceId)).thenReturn(-1l);
		ArgumentCaptor<CallForPapers> cfpArgumentCaptor = ArgumentCaptor.forClass(CallForPapers.class);
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		MvcResult expectedResponse = mockMvc
				.perform(post(baseUrl + "/add/" + conferenceId).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(callForPapers)))
				.andExpect(status().isBadRequest()).andReturn();

		// then
		//verify(callForPapersService).add(cfpArgumentCaptor.capture(), conferenceIdArgumentCaptor.capture());
		assertThat(cfpArgumentCaptor.getValue()).isEqualTo(callForPapers);
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conferenceId);
		assertThat(getResponseContent(expectedResponse))
				.isEqualTo("We cannot add call for papers because the corresponding conference does not exist");
	}

	@Test
	void shouldGetCFPByConferenceId() throws Exception {
		// when
		when(callForPapersService.findByConferenceId(conferenceId)).thenReturn(Optional.of(callForPapers));
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/" + conferenceId)).andExpect(status().isOk())
				.andReturn();

		// then
		verify(callForPapersService).findByConferenceId(conferenceIdArgumentCaptor.capture());
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conferenceId);
		assertThat(getResponseContent(expectedResponse)).isEqualTo(objectMapper.writeValueAsString(callForPapers));
	}

	@Test
	void shouldNotGetCFPByConferenceId() throws Exception {
		// when
		when(callForPapersService.findByConferenceId(conferenceId)).thenReturn(Optional.empty());
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/" + conferenceId))
				.andExpect(status().isNoContent()).andReturn();

		// then
		verify(callForPapersService).findByConferenceId(conferenceIdArgumentCaptor.capture());
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conferenceId);
		assertThat(getResponseContent(expectedResponse)).isEmpty();
	}

	@Test
	void shouldCheckIfCFPExistsByConferenceId() throws Exception {
		// when
		when(callForPapersService.existsByConferenceId(conferenceId)).thenReturn(true);
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/exists/" + conferenceId))
				.andExpect(status().isOk()).andReturn();

		// then
		verify(callForPapersService).existsByConferenceId(conferenceIdArgumentCaptor.capture());
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conferenceId);
		assertThat(Boolean.valueOf(getResponseContent(expectedResponse))).isTrue();

	}

	private String getResponseContent(MvcResult expectedResponse) throws UnsupportedEncodingException {
		return expectedResponse.getResponse().getContentAsString();
	}

}
