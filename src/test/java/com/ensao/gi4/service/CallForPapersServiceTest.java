package com.ensao.gi4.service;

import com.ensao.gi4.dto.CallForPapersRequestDto;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.repository.CallForPapersRepository;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.service.api.CallForPapersService;
import com.ensao.gi4.service.impl.CallForPapersServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CallForPapersServiceTest {

	@Mock
	private ConferenceRepository conferenceRepository;
	@Mock
	private CallForPapersRepository callForPapersRepository;
	private CallForPapersService underTest;
	Conference conference;
	CallForPapers callForPapers;
	private Set<String> topics;

	@BeforeEach
	void setUp() {
		underTest = new CallForPapersServiceImpl(callForPapersRepository, conferenceRepository);

		// given
        conference = new Conference(
                "International Conference",
                "IC",
                "UMP",
                "Oujda",
                "Morocco",
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                "Computer Science",
                "Artificial Intelligence",
                "organizeName");
		conference.setId(1L);

		topics = new HashSet<>();
		topics.add("Medical");
		topics.add("Agricultural");
		topics.add("Automotive");
        topics.add("Education");

		callForPapers = new CallForPapers();
		callForPapers.setStartDate(LocalDate.of(2022, 4, 10));
		callForPapers.setEndDate(LocalDate.of(2022, 6, 30));
		callForPapers.setGuidelines("Guidelines instruction");
		callForPapers.setTopics(topics);
	}

	@Test
	void shouldAddCFP() throws JsonProcessingException {
		// given
		var callForPapersDto = new CallForPapersRequestDto(
                "10/04/2022",
                "30/06/2022",
                topics,
                "Guidelines instruction");
		callForPapers.setTopics(topics);
		callForPapers.setConference(conference);

		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		when(callForPapersRepository.save(callForPapers)).thenReturn(callForPapers);
		ArgumentCaptor<CallForPapers> cfpArgumentCaptor = ArgumentCaptor.forClass(CallForPapers.class);

		Optional<CallForPapers> optionalCFP = underTest.add(callForPapersDto, conference.getId());

		// then
		verify(callForPapersRepository).save(cfpArgumentCaptor.capture());
		assertThat(cfpArgumentCaptor.getValue()).isEqualTo(callForPapers);
		assertThat(optionalCFP).isNotEmpty();
		assertThat(optionalCFP).hasValue(callForPapers);
	}

	@Test
	void shouldNotAddCFP() throws JsonProcessingException {
		// given
        var callForPapersDto = new CallForPapersRequestDto(
                "10/04/2022",
                "30/04/2022",
                topics,
                "Guidelines instruction");
		callForPapers.setTopics(topics);
		callForPapers.setConference(conference);

		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.empty());
		Optional<CallForPapers> optionalCFP = underTest.add(callForPapersDto, conference.getId());

		// then
		assertThat(optionalCFP).isEmpty();

	}

	@Test
	void shouldFindCFPByConferenceId() {
		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		when(callForPapersRepository.findByConference(conference)).thenReturn(Optional.of(callForPapers));
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Conference> conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);

		Optional<CallForPapers> optionalCFP = underTest.findByConferenceId(conference.getId());

		// then
		assertThat(optionalCFP).isNotEmpty();
		verify(conferenceRepository).findById(conferenceIdArgumentCaptor.capture());
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conference.getId());

		verify(callForPapersRepository).findByConference(conferenceArgumentCaptor.capture());
		assertThat(conferenceArgumentCaptor.getValue()).isEqualTo(conference);

	}

	@Test
	void shouldCheckIfCFPExists() {
		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		when(callForPapersRepository.existsByConference(conference)).thenReturn(true);
		boolean exists = underTest.existsByConferenceId(conference.getId());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Conference> conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);

		// then
		assertThat(exists).isEqualTo(true);
		verify(conferenceRepository).findById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId());
		verify(callForPapersRepository).existsByConference(conferenceArgumentCaptor.capture());
		assertThat(conferenceArgumentCaptor.getValue()).isEqualTo(conference);

	}

}
