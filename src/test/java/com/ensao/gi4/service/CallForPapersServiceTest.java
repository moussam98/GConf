package com.ensao.gi4.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Topic;
import com.ensao.gi4.repository.CallForPapersRepository;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.repository.TopicRepository;
import com.ensao.gi4.service.api.CallForPapersService;
import com.ensao.gi4.service.impl.CallForPapersServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ExtendWith(MockitoExtension.class)
public class CallForPapersServiceTest {

	@Mock
	private ConferenceRepository conferenceRepository;
	@Mock
	private CallForPapersRepository callForPapersRepository;
	@Mock
	private TopicRepository topicRepository;
	private CallForPapersService underTest;
	Conference conference;
	CallForPapers callForPapers;
	private Set<Topic> topics;

	@BeforeEach
	void setUp() {
		underTest = new CallForPapersServiceImpl(callForPapersRepository, conferenceRepository, topicRepository);

		// given
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conference.setId(1l);

		Topic topic1 = new Topic(null, "Medical");
		Topic topic2 = new Topic(null, "Agricultural");
		Topic topic3 = new Topic(null, "Automotive");
		Topic topic4 = new Topic(null, "Education");

		topics = new HashSet<>();
		topics.add(topic1);
		topics.add(topic2);
		topics.add(topic3);
		topics.add(topic4);

		callForPapers = new CallForPapers();
		callForPapers.setStartDate(LocalDate.of(2022, 4, 10));
		callForPapers.setEndDate(LocalDate.of(2022, 6, 30));
		callForPapers.setGuidelines("Guidelines instruction");
		callForPapers.setTopics(topics);
	}

	@Test
	void shouldAddCFP() throws JsonMappingException, JsonProcessingException {
		// given
		CallForPapersDto callForPapersDto = new CallForPapersDto("10/04/2022", "30/6/2022", topics,
				"Guidelines instruction");
		callForPapers.setTopics(topics);
		callForPapers.setConference(conference);

		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		when(topicRepository.saveAll(topics)).thenReturn(topics.stream().collect(Collectors.toList()));
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
	void shouldNotAddCFP() throws JsonMappingException, JsonProcessingException {
		// given
		CallForPapersDto callForPapersDto = new CallForPapersDto("10/10/2022", "30/8/2022", topics,
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
		when(conferenceRepository.existsById(conference.getId())).thenReturn(true);
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		when(callForPapersRepository.existsByConference(conference)).thenReturn(true);
		boolean exists = underTest.existsByConferenceId(conference.getId());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Conference> conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);

		// then
		assertThat(exists).isEqualTo(true);
		verify(conferenceRepository).existsById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId());
		verify(conferenceRepository).findById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId());
		verify(callForPapersRepository).existsByConference(conferenceArgumentCaptor.capture());
		assertThat(conferenceArgumentCaptor.getValue()).isEqualTo(conference);

	}

}
