package com.ensao.gi4.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
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

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferenceFirstInfoDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Role;
import com.ensao.gi4.model.User;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.repository.UserRepository;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.service.impl.ConferenceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ConferenceServiceTest {

	@Mock
	private ConferenceRepository conferenceRepository;
	@Mock
	private UserRepository userRepository;
	private ConferenceService underTest;
	private Conference conference;
	private User user;

	@BeforeEach
	void setUp() {
		underTest = new ConferenceServiceImpl(conferenceRepository, userRepository);
		conference = new Conference("International Confernce", "GConf", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conference.setId(1l);

		user = new User();
		user.setId(1l);
		user.setFirstname("Ali");
		user.setLastname("Moussa");
		user.setEmail("ali@gmail.com");
		user.setRole(Role.ADMIN);

	}

	@Test
	void shouldAddConference() {
		// given
		ConferenceFirstInfoDto conferenceDto = new ConferenceFirstInfoDto();
		conferenceDto.setName("International Confernce");
		conferenceDto.setAcronym("GConf");

		Conference conference = Mapper.firstInfoToConference(conferenceDto);
		conference.setUser(user);

		Conference savedconference = new Conference();
		savedconference.setName("International Confernce");
		savedconference.setAcronym("GConf");
		savedconference.setId(1l);
		savedconference.setUser(user);

		// when
		when(conferenceRepository.existsByName(conference.getName())).thenReturn(false);
		when(conferenceRepository.existsByAcronym(conference.getAcronym())).thenReturn(false);
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(conferenceRepository.save(conference)).thenReturn(savedconference);

		ArgumentCaptor<Conference> conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);
		ArgumentCaptor<String> currentStringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

		Long result = underTest.add(conferenceDto, user.getId());

		// then
		assertThat(result).isEqualTo(savedconference.getId());
		verify(conferenceRepository, times(1)).save(conferenceArgumentCaptor.capture());
		assertThat(conferenceArgumentCaptor.getValue()).isEqualTo(conference);

		verify(conferenceRepository, times(1)).existsByName(currentStringArgumentCaptor.capture());
		assertThat(currentStringArgumentCaptor.getValue()).isEqualTo(conference.getName());

		verify(conferenceRepository, times(1)).existsByAcronym(currentStringArgumentCaptor.capture());
		assertThat(currentStringArgumentCaptor.getValue()).isEqualTo(conference.getAcronym());

		verify(userRepository, times(1)).findById(userIdArgumentCaptor.capture());
		assertThat(userIdArgumentCaptor.getValue()).isEqualTo(user.getId());
	}

	@Test
	void shouldNotAddConferenceIfExists() {
		// given
		ConferenceFirstInfoDto conferenceDto = new ConferenceFirstInfoDto();
		conferenceDto.setName("International Confernce");
		conferenceDto.setAcronym("GConf");

		// when
		when(conferenceRepository.existsByName(conference.getName())).thenReturn(true);
		when(conferenceRepository.existsByAcronym(conference.getAcronym())).thenReturn(true);
		Long result = underTest.add(conferenceDto, user.getId());

		// then
		assertThat(result).isEqualTo(-1);
	}

	@Test
	void shouldReturnConferenceIfExists() {
		// when
		when(conferenceRepository.findByName(conference.getName())).thenReturn(Optional.of(conference));
		Optional<Conference> optionalConference = underTest.findByName(conference.getName());
		ArgumentCaptor<String> conferenceNameArgumentCaptor = ArgumentCaptor.forClass(String.class);

		// then
		assertThat(optionalConference).isNotEmpty();
		assertThat(optionalConference).hasValue(conference);
		verify(conferenceRepository, times(1)).findByName(conferenceNameArgumentCaptor.capture());
		assertThat(conferenceNameArgumentCaptor.getValue()).isEqualTo(conference.getName());
	}

	@Test
	void shouldReturnEmptyIfConferenceDoesNotExists() {
		// when
		when(conferenceRepository.findByAcronym(conference.getAcronym())).thenReturn(Optional.empty());
		Optional<Conference> expectedConference = underTest.findByAcronym(conference.getAcronym());
		ArgumentCaptor<String> conferenceAcronymArgumentCaptor = ArgumentCaptor.forClass(String.class);

		// then
		assertThat(expectedConference).isEmpty();
		verify(conferenceRepository, times(1)).findByAcronym(conferenceAcronymArgumentCaptor.capture());
		assertThat(conferenceAcronymArgumentCaptor.getValue()).isEqualTo(conference.getAcronym());
	}

	@Test
	void shouldUpdateConferenceById() {
		// given

		ConferenceDto conferenceDto = new ConferenceDto();
		conferenceDto.setName("International Confernce");
		conferenceDto.setAcronym("GConf");
		conferenceDto.setVenue("UMP");
		conferenceDto.setCity("Oujda");
		conferenceDto.setCountry("Morocco");
		conferenceDto.setFirstDay("10/10/2022");
		conferenceDto.setLastDay("13/10/2022");
		conferenceDto.setPrimaryArea("Computer Science");
		conferenceDto.setSecondaryArea("Artificial Intelligence");
		conferenceDto.setOrganizer("organizeName");
		conferenceDto.setPhoneNumber("+55121131");
		conferenceDto.setOtherInfo("Other info");

		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);

		Optional<Conference> optionalConference = underTest.updateConferenceById(conference.getId(), conferenceDto);

		// then
		verify(conferenceRepository, times(1)).findById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId());
		assertThat(optionalConference).isNotEmpty();
		assertThat(optionalConference).hasValue(conference);
	}

	@Test
	void shouldNoUpdateConferenceIfDoesNotExists() {
		// given

		ConferenceDto conferenceDto = new ConferenceDto();
		conferenceDto.setName("International Confernce");
		conferenceDto.setAcronym("GConf");
		conferenceDto.setVenue("UMP");
		conferenceDto.setCity("Oujda");
		conferenceDto.setCountry("Morocco");
		conferenceDto.setFirstDay("10/10/2022");
		conferenceDto.setLastDay("13/10/2022");
		conferenceDto.setPrimaryArea("Computer Science");
		conferenceDto.setSecondaryArea("Artificial Intelligence");
		conferenceDto.setOrganizer("organizeName");
		conferenceDto.setPhoneNumber("+55121131");
		conferenceDto.setOtherInfo("Other info");

		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.empty());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		Optional<Conference> optionalConference = underTest.updateConferenceById(conference.getId(), conferenceDto);
		
		// then
		verify(conferenceRepository, times(1)).findById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId());
		assertThat(optionalConference).isEmpty();
	}

}
