package com.ensao.gi4.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Topic;
import com.ensao.gi4.repository.CallForPapersRepository;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.repository.TopicRepository;
import com.ensao.gi4.service.api.CallForPapersService;
import com.ensao.gi4.service.impl.CallForPapersServiceImpl;

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

	@BeforeEach
	void setUp() {
		underTest = new CallForPapersServiceImpl(callForPapersRepository, conferenceRepository, topicRepository);

		//given 
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conference.setId(1l);
		
		Topic topic1 = new Topic(null, "Medical");
		Topic topic2 = new Topic(null, "Agricultural");
		Topic topic3 = new Topic(null, "Automotive");
		Topic topic4 = new Topic(null, "Education");
		
//		Topic topic1 = new Topic(null, "Medical", null);
//		Topic topic2 = new Topic(null, "Agricultural", null);
//		Topic topic3 = new Topic(null, "Automotive", null);
//		Topic topic4 = new Topic(null, "Education", null);
		
		Set<Topic> topics = new HashSet<>(); 
		topics.add(topic1);
		topics.add(topic2);
		topics.add(topic3);
		topics.add(topic4);

		callForPapers = new CallForPapers();
		callForPapers.setId(1l);
		callForPapers.setStartDate(LocalDate.now());
		callForPapers.setEndDate(LocalDate.of(2022, 8, 30));
		callForPapers.setGuidelines("Guidelines instruction");
		callForPapers.setTopics(topics);
	}

	@Test
	void shouldAddCFP() {
		// when
		when(conferenceRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(true);
		when(conferenceRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(conference));
		when(callForPapersRepository.save(callForPapers)).thenReturn(callForPapers); 
		//Long actualResult = underTest.add(callForPapers, conference.getId());
		ArgumentCaptor<CallForPapers> cfpArgumentCaptor = ArgumentCaptor.forClass(CallForPapers.class);

		// then
		verify(callForPapersRepository).save(cfpArgumentCaptor.capture());
	//	assertThat(actualResult).isEqualTo(callForPapers.getId());
		assertThat(callForPapers).isEqualTo(cfpArgumentCaptor.getValue());
		assertThat(cfpArgumentCaptor.getValue().getConference()).isEqualTo(conference);
	}

	@Test
	void shouldNotAddCFP() {
		// when
		when(conferenceRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(false);
		//Long actualResult = underTest.add(callForPapers, conference.getId());

		// then
		//assertThat(actualResult).isEqualTo(-1);

	}

	@Test
	void shouldFindCFPByConferenceId() {
		// when
		when(conferenceRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(true);
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		//when(callForPapersRepository.findByConference(conference)).thenReturn(Optional.of(callForPapers));
		Optional<CallForPapers> cfpOptional = underTest.findByConferenceId(conference.getId());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Conference> conferenceArgumentCaptor = ArgumentCaptor.forClass(Conference.class);

		// then
		assertThat(cfpOptional).isNotEmpty();
		verify(conferenceRepository).existsById(idArgumentCaptor.capture());
		verify(conferenceRepository).findById(idArgumentCaptor.capture());
		//verify(callForPapersRepository).findByConference(conferenceArgumentCaptor.capture());

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
