package com.ensao.gi4.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ensao.gi4.model.Conference;
import com.ensao.gi4.service.api.ConferenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ConferenceController.class)
public class ConferenceControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private ConferenceService conferenceService;
	private String baseUrl = "/api/v1/conference";
	private Conference conference;

	@BeforeEach
	void setUp() {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conference.setId(1l);
	}

	@Test
	void canAddConference() throws Exception {
		// when
//		when(conferenceService.add(conference)).thenReturn(conference.getId());
//		ArgumentCaptor<Conference> conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);
//		MvcResult expectedResponse = mockMvc.perform(post(baseUrl + "/add").contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(conference))).andExpect(status().isOk()).andReturn();

		// then
//		verify(conferenceService).add(conferenceArgumentCaptor.capture());
//		assertThat(conferenceArgumentCaptor.getValue()).isEqualTo(conference);
//		assertThat(getResponseContent(expectedResponse)).isEqualTo(conference.getId().toString());
	}

	@Test
	void cannotAddConference() throws JsonProcessingException, Exception {
		// when
//		when(conferenceService.add(conference)).thenReturn(-1l);
//		MvcResult expectedResponse = mockMvc
//				.perform(post(baseUrl + "/add").contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(conference)))
//				.andExpect(status().isBadRequest()).andReturn();

		// then
//		assertThat(getResponseContent(expectedResponse))
//				.isEqualTo("Unable to add this conference because its name and acronym already exist");
	}

	@Test
	void shouldGetConferenceById() throws JsonProcessingException, Exception {
		// when
		Long conferenceId = conference.getId();
		when(conferenceService.findById(conferenceId)).thenReturn(Optional.of(conference));
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/" + conferenceId)).andExpect(status().isOk())
				.andReturn();

		// then
		assertThat(getResponseContent(expectedResponse)).isEqualTo(objectMapper.writeValueAsString(conference));
		verify(conferenceService).findById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId());

	}

	@Test
	void shouldNotReturnConferenceById() throws Exception {
		// when
		Long conferenceId = conference.getId();
		when(conferenceService.findById(conferenceId)).thenReturn(Optional.empty());
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/" + conferenceId))
				.andExpect(status().isNoContent()).andReturn();

		// then
		assertThat(getResponseContent(expectedResponse)).isEmpty();

	}

	@Test
	void shouldGetConferenceByName() throws JsonProcessingException, Exception {
		// when
		String conferenceName = conference.getName();
		when(conferenceService.findByName(conferenceName)).thenReturn(Optional.of(conference));
		ArgumentCaptor<String> nameArgumentCaptor = ArgumentCaptor.forClass(String.class);
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/name/" + conferenceName))
				.andExpect(status().isOk()).andReturn();

		// then
		assertThat(getResponseContent(expectedResponse)).isEqualTo(objectMapper.writeValueAsString(conference));
		verify(conferenceService).findByName(nameArgumentCaptor.capture());
		assertThat(nameArgumentCaptor.getValue()).isEqualTo(conference.getName());

	}

	@Test
	void shouldNotReturnConferenceByName() throws Exception {
		// when
		String conferenceName = conference.getName();
		when(conferenceService.findByName(conferenceName)).thenReturn(Optional.empty());
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/name/" + conferenceName))
				.andExpect(status().isNoContent()).andReturn();

		// then
		assertThat(getResponseContent(expectedResponse)).isEmpty();

	}

	@Test
	void shouldGetConferenceByAcronym() throws JsonProcessingException, Exception {
		// when
		String conferenceAcronym = conference.getAcronym();
		when(conferenceService.findByAcronym(conferenceAcronym)).thenReturn(Optional.of(conference));
		ArgumentCaptor<String> acronymArgumentCaptor = ArgumentCaptor.forClass(String.class);
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/acronym/" + conferenceAcronym))
				.andExpect(status().isOk()).andReturn();

		// then
		assertThat(getResponseContent(expectedResponse)).isEqualTo(objectMapper.writeValueAsString(conference));
		verify(conferenceService).findByAcronym(acronymArgumentCaptor.capture());
		assertThat(acronymArgumentCaptor.getValue()).isEqualTo(conference.getAcronym());

	}

	@Test
	void shouldNotReturnConferenceByAcronym() throws Exception {
		// when
		String conferenceAcronym = conference.getAcronym();
		when(conferenceService.findByAcronym(conferenceAcronym)).thenReturn(Optional.empty());
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/acronym/" + conferenceAcronym))
				.andExpect(status().isNoContent()).andReturn();

		// then
		assertThat(getResponseContent(expectedResponse)).isEmpty();

	}

	@Test
	void shouldCheckIfConferenceExistsById() throws Exception {
		// when
		Long conferenceId = conference.getId();
		when(conferenceService.existsById(conferenceId)).thenReturn(true);
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/exists/" + conferenceId))
				.andExpect(status().isOk()).andReturn();

		// then
		verify(conferenceService).existsById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId());
		assertThat(Boolean.valueOf(getResponseContent(expectedResponse))).isTrue();

	}

	@Test
	void shouldUpdatedConferenceById() throws Exception {
		// given 
		Conference updatedConference = new Conference("International Confernce I", "ICI", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		// when
		Long conferenceId = conference.getId();
	//	when(conferenceService.updateConferenceById(conferenceId, updatedConference)).thenReturn(true); 
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Conference> conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);
		MvcResult expectedResponse = mockMvc.perform(put(baseUrl + "/update/" + conferenceId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedConference)))
				.andExpect(status().isOk()).andReturn();
		
		// then 
		//verify(conferenceService).updateConferenceById(idArgumentCaptor.capture(), conferenceArgumentCaptor.capture()); 
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conferenceId); 
		assertThat(conferenceArgumentCaptor.getValue()).isEqualTo(updatedConference);
		assertThat(Boolean.valueOf(getResponseContent(expectedResponse))).isTrue(); 
	}
	
	

	private String getResponseContent(MvcResult expectedResponse) throws UnsupportedEncodingException {
		return expectedResponse.getResponse().getContentAsString();
	}

}
