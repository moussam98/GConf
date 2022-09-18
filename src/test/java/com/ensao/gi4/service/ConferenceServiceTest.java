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
import com.ensao.gi4.repository.UserRepository;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.service.impl.ConferenceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ConferenceServiceTest {

	@Mock
	private ConferenceRepository conferenceRepository;
	private UserRepository userRepository;
	private ConferenceService underTest;
	private Conference conference;

	@BeforeEach
	void setUp() {
		underTest = new ConferenceServiceImpl(conferenceRepository, userRepository);
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conference.setId(1l);
	}

	@Test
	void shouldAddConference() {
		// when
		when(conferenceRepository.save(conference)).thenReturn(conference);
		when(conferenceRepository.existsByName(conference.getName())).thenReturn(false);
		when(conferenceRepository.existsByAcronym(conference.getAcronym())).thenReturn(false);
//		Long result = underTest.add(conference);
		ArgumentCaptor<Conference> argumentCaptor = ArgumentCaptor.forClass(Conference.class);
		ArgumentCaptor<String> currentStringArgumentCaptor = ArgumentCaptor.forClass(String.class);

		// then
//		assertThat(result).isEqualTo(conference.getId());
		verify(conferenceRepository).save(argumentCaptor.capture());
		assertThat(argumentCaptor.getValue()).isEqualTo(conference);
		verify(conferenceRepository).existsByName(currentStringArgumentCaptor.capture());
		assertThat(currentStringArgumentCaptor.getValue()).isEqualTo(conference.getName());
		verify(conferenceRepository).existsByAcronym(currentStringArgumentCaptor.capture());
		assertThat(currentStringArgumentCaptor.getValue()).isEqualTo(conference.getAcronym());
	}

	@Test
	void shouldNotAddConferenceIfExists() {
		// when
		when(conferenceRepository.existsByName(conference.getName())).thenReturn(true);
		when(conferenceRepository.existsByAcronym(conference.getAcronym())).thenReturn(true);
//		Long result = underTest.add(conference);

		// then
//		assertThat(result).isEqualTo(-1);
	}

	@Test
	void shouldReturnConferenceIfExists() {
		// when
		//when(conferenceRepository.findByName(conference.getName())).thenReturn(Optional.of(conference));
		Optional<Conference> expectedConference = underTest.findByName(conference.getName());
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

		// then
		assertThat(expectedConference).isNotEmpty();
		assertThat(expectedConference).hasValue(conference);
		//verify(conferenceRepository).findByName(argumentCaptor.capture());
	}

	@Test
	void shouldReturnEmptyIfConferenceDoesNotExists() {
		// when
		//when(conferenceRepository.findByAcronym(conference.getAcronym())).thenReturn(Optional.empty());
		Optional<Conference> expectedConference = underTest.findByAcronym(conference.getAcronym());
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

		// then
		assertThat(expectedConference).isEmpty();
		//verify(conferenceRepository).findByAcronym(argumentCaptor.capture());
		assertThat(argumentCaptor.getValue()).isEqualTo(conference.getAcronym());
	}

	@Test
	void shouldUpdateConferenceById() {
		// given
		Conference updatedConference = new Conference("International Confernce I", "ICI", "UMP", "Oujda", "Morrocco",
				LocalDate.now(), LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence",
				"organizeName");
		
		// when 
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class); 
		//boolean expectedResult = underTest.updateConferenceById(conference.getId(), updatedConference);
		
		// then 
		verify(conferenceRepository).findById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId()); 
		//assertThat(expectedResult).isTrue(); 
	}
	
	@Test
	void shouldNoUpdateConferenceIfDoesNotExists() {
		// given
		Conference updatedConference = new Conference("International Confernce I", "ICI", "UMP", "Oujda", "Morrocco",
				LocalDate.now(), LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence",
				"organizeName");
		
		// when 
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.empty());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class); 
		//boolean expectedResult = underTest.updateConferenceById(conference.getId(), updatedConference);
		
		// then 
		verify(conferenceRepository).findById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId()); 
	//	assertThat(expectedResult).isFalse(); 
	}

}
