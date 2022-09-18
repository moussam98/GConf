//package com.ensao.gi4.controller;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import com.ensao.gi4.model.Person;
//import com.ensao.gi4.service.api.PersonService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@WebMvcTest(PersonController.class)
//public class PersonControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//	@Autowired
//	private ObjectMapper objectMapper;
//	@MockBean
//	private PersonService personService;
//	private String baseUrl = "/api/v1/person";
//	private Person person;
//
//	@BeforeEach
//	void setUp() {
//		person = new Person(1l, "Ali", "Moussa", "ali@gmail.com", "Morocco", "ENSAO", null);
//	}
//
//	@Test
//	void shouldAddPerson() throws JsonProcessingException, Exception {
//		// when
//		when(personService.addPerson(person)).thenReturn(person.getId());
//		ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
//		MvcResult expectedResponse = mockMvc.perform(post(baseUrl + "/add").contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(person))).andExpect(status().isOk()).andReturn();
//
//		// then
//		verify(personService).addPerson(personArgumentCaptor.capture());
//		assertThat(personArgumentCaptor.getValue()).isEqualTo(person);
//		assertThat(getResponseContent(expectedResponse)).isEqualTo(person.getId().toString());
//
//	}
//
//	private String getResponseContent(MvcResult actualResult) throws UnsupportedEncodingException {
//		return actualResult.getResponse().getContentAsString();
//	}
//
//	@Test
//	void shouldNotAddPersonIfEmailExist() throws JsonProcessingException, Exception {
//		// when
//		when(personService.addPerson(person)).thenReturn(-1l);
//		MvcResult expectedResponse = mockMvc
//				.perform(post(baseUrl + "/add").contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(person)))
//				.andExpect(status().isBadRequest()).andReturn();
//
//		// then
//		assertThat(getResponseContent(expectedResponse)).contains("We cannot add a person because email already exists !");
//	}
//
//	@Test
//	void shouldGetPersonIfEmailExist() throws Exception {
//		// given
//		String email = "ali@gmail.com";
//
//		// when
//		when(personService.findByEmail(email)).thenReturn(Optional.of(person));
//		ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
//		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/" + email)).andExpect(status().isOk()).andReturn();
//
//		// then
//		verify(personService).findByEmail(emailArgumentCaptor.capture());
//		assertThat(emailArgumentCaptor.getValue()).isEqualTo(email);
//		assertThat(getResponseContent(expectedResponse)).isEqualTo(objectMapper.writeValueAsString(person));
//
//	}
//
//	@Test
//	void shouldReturnEmptyIfEmailDoesNotExits() throws Exception {
//		// given
//		String email = "ali@gmail.com";
//
//		// when
//		when(personService.findByEmail(email)).thenReturn(Optional.empty());
//		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/" + email)).andExpect(status().isNoContent())
//				.andReturn();
//
//		// then
//		assertThat(getResponseContent(expectedResponse)).isEmpty();
//
//	}
//
//	@Test
//	void shouldGetAllPersons() throws Exception {
//		// given
//		List<Person> personList = new ArrayList<>();
//		personList.add(person);
//		personList.add(new Person(2l, "Ibrahim", "Moussa", "ibrahim@gmail.com", "Morocco", "ENSAO", null));
//		personList.add(new Person(3l, "Ibrahim", "Moussa", "ibrahim@gmail.com", "Morocco", "ENSAO", null));
//		personList.add(new Person(4l, "Issa", "Moussa", "issa@gmail.com", "Morocco", "ENSAO", null));
//
//		// when
//		when(personService.findAll()).thenReturn(Optional.of(personList));
//		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/findAll")).andExpect(status().isOk()).andReturn();
//
//		// then
//		verify(personService).findAll();
//		List<Person> expectedPersonList = objectMapper.readValue(getResponseContent(expectedResponse),
//				new TypeReference<List<Person>>() {
//				});
//		assertThat(expectedPersonList).containsAll(personList);
//	}
//	
//	@Test
//	void shouldNotFindPersons() throws Exception {
//		// when
//		when(personService.findAll()).thenReturn(Optional.empty()); 
//		MvcResult expectedResponse = mockMvc.perform(get(baseUrl + "/findAll")).andExpect(status().isNoContent()).andReturn();
//		
//		// then
//		assertThat(getResponseContent(expectedResponse)).isEmpty(); 
//	}
//	
//	@Test
//	void shouldDeletePersonIfEmailExists() throws Exception {
//		// given 
//		String email ="ali@gmail.com"; 
//		
//		// when 
//		when(personService.deleteByEmail(email)).thenReturn(1); 
//		MvcResult expectedResponse = mockMvc.perform(delete(baseUrl + "/delete/"+ email)).andExpect(status().isOk()).andReturn();
//		
//		// then 
//		assertThat(getResponseContent(expectedResponse)).isEqualTo("Person deleted !"); 
//	}
//	
//	@Test
//	void shouldNotDeletePersonIfEmailDoesExists() throws Exception {
//		// given 
//		String email ="ali@gmail.com"; 
//		
//		// when 
//		when(personService.deleteByEmail(email)).thenReturn(-1); 
//		MvcResult expectedResponse = mockMvc.perform(delete(baseUrl + "/delete/"+ email)).andExpect(status().isNotFound()).andReturn();
//		
//		// then 
//		assertThat(getResponseContent(expectedResponse)).isEqualTo("Person not found !"); 
//	}
//}
