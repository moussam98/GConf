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
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.repository.CallForPapersRepository;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.service.api.CallForPapersService;
import com.ensao.gi4.service.impl.CallForPapersServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CallForPapersServiceTest {

	@Mock
	private ConferenceRepository conferenceRepository;
	@Mock
	private CallForPapersRepository callForPapersRepository;
	private CallForPapersService underTest;
	Conference conference;
	CallForPapers callForPapers;

	@BeforeEach
	void setUp() {
		underTest = new CallForPapersServiceImpl(callForPapersRepository, conferenceRepository);

		//given 
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conference.setId(1l);

		callForPapers = new CallForPapers();
		callForPapers.setStartDate(LocalDate.now());
		callForPapers.setEndDate(LocalDate.of(2022, 8, 30));
		callForPapers.setGuidelines("Guidelines instruction");
		callForPapers.setTopic1("Medical");
		callForPapers.setTopic2("Agricultural");
		callForPapers.setTopic3("Automotive");
		callForPapers.setTopic4("Education");
	}

	@Test
	void shouldAddCFP() {
		// when
		when(conferenceRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(true);
		when(conferenceRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(conference));
		int actualResult = underTest.add(callForPapers, conference.getId());
		ArgumentCaptor<CallForPapers> cfpArgumentCaptor = ArgumentCaptor.forClass(CallForPapers.class);

		// then
		verify(callForPapersRepository).save(cfpArgumentCaptor.capture());
		assertThat(actualResult).isEqualTo(1);
		assertThat(callForPapers).isEqualTo(cfpArgumentCaptor.getValue());
		assertThat(cfpArgumentCaptor.getValue().getConference()).isEqualTo(conference);
	}

	@Test
	void shouldNotAddCFP() {
		// when
		when(conferenceRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(false);
		int actualResult = underTest.add(callForPapers, conference.getId());

		// then
		assertThat(actualResult).isEqualTo(0);

	}

	@Test
	void shouldFindCFPByConferenceId() {
		// when
		when(conferenceRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(true);
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		when(callForPapersRepository.findByConference(conference)).thenReturn(Optional.of(callForPapers));
		Optional<CallForPapers> cfpOptional = underTest.findByConferenceId(conference.getId());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Conference> conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);

		// then
		assertThat(cfpOptional).isNotEmpty();
		verify(conferenceRepository).existsById(idArgumentCaptor.capture());
		verify(conferenceRepository).findById(idArgumentCaptor.capture());
		verify(callForPapersRepository).findByConference(conferenceArgumentCaptor.capture());

	}
	
	@Test
	void shouldNotFindCFP() {
		// when 
		when(conferenceRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(false);
		Optional<CallForPapers> cfpOptional = underTest.findByConferenceId(conference.getId()); 
		
		// then
		assertThat(cfpOptional).isEmpty(); 
		
	}

	@Test
	void shouldCheckIfCFPExists() {
		// when
		when(conferenceRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(true);
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		when(callForPapersRepository.existsByConference(conference)).thenReturn(true);
		boolean exists = underTest.existsByConferenceId(conference.getId());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Conference> conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);

		// then
		assertThat(exists).isEqualTo(true);
		verify(conferenceRepository).existsById(idArgumentCaptor.capture());
		verify(conferenceRepository).findById(idArgumentCaptor.capture());
		verify(callForPapersRepository).existsByConference(conferenceArgumentCaptor.capture());

	}
	
	@Test
	void shouldCheckCFPDoesNotExist() {
		// when 
		when(conferenceRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(false);
		boolean exists = underTest.existsByConferenceId(conference.getId()); 
		
		// when
		assertThat(exists).isEqualTo(false); 
		
	}
}
