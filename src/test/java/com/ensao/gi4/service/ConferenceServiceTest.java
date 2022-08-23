package com.ensao.gi4.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ensao.gi4.model.Conference;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.service.impl.ConferenceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ConferenceServiceTest {

	@Mock
	private ConferenceRepository conferenceRepository;
	private ConferenceService underTest;

	@BeforeEach
	void setUp() {
		underTest = new ConferenceServiceImpl(conferenceRepository);
	}

	@Test
	void shouldAddConference() {
		// given
		Conference conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco",
				LocalDate.now(), LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence",
				"organizeName");
		
		// when 
		int result = underTest.add(conference); 
		ArgumentCaptor<Conference> argumentCaptor = ArgumentCaptor.forClass(Conference.class);
		
		// then
		assertThat(result).isEqualTo(1);
		verify(conferenceRepository).save(argumentCaptor.capture()); 
		assertThat(argumentCaptor.getValue()).isEqualTo(conference);	
	}
	
	@Test
	void shouldNotAddConferenceIfExists() {
		// given
		Conference conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco",
				LocalDate.now(), LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence",
				"organizeName");
		
		// when 
		when(conferenceRepository.existsByName(conference.getName())).thenReturn(true);
		int result = underTest.add(conference); 
		
		// then
		assertThat(result).isEqualTo(0);
	}
	
	@Test
	void shouldReturnConferenceIfExists() {
		// given 
		Conference conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco",
				LocalDate.now(), LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence",
				"organizeName");
		underTest.add(conference); 
		
		// when 
		when(conferenceRepository.findByName(conference.getName())).thenReturn(Optional.of(conference));
		Optional<Conference> expectedConference = underTest.findByName(conference.getName()); 
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		
		// then 
		assertThat(expectedConference).isNotEmpty(); 
		assertThat(expectedConference).hasValue(conference); 
		verify(conferenceRepository).findByName(argumentCaptor.capture());
	}
	
	@Test
	void shouldReturnEmptyIfConferenceDoesNotExists() {
		// given 
		Conference conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco",
				LocalDate.now(), LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence",
				"organizeName");
		underTest.add(conference); 
		
		// when 
		when(conferenceRepository.findByAcronym(conference.getAcronym())).thenReturn(Optional.empty());
		Optional<Conference> expectedConference = underTest.findByAcronym(conference.getAcronym()); 
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		
		// then 
		assertThat(expectedConference).isEmpty(); 
		verify(conferenceRepository).findByAcronym(argumentCaptor.capture()); 
		assertThat(argumentCaptor.getValue()).isEqualTo(conference.getAcronym());	
	}
	
	
	
	

}
