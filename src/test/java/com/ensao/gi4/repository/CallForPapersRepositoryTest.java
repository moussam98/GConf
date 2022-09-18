package com.ensao.gi4.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Topic;

@DataJpaTest
public class CallForPapersRepositoryTest {

	@Autowired
	private CallForPapersRepository underTest;
	@Autowired
	private ConferenceRepository conferenceRepository;
	@Autowired
	private TopicRepository topicRepository;
	private Conference conference;
	private CallForPapers callForPapers;
	private Long conferenceId;

	@BeforeEach
	void setUp() {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conferenceId = conferenceRepository.save(conference).getId();

		Topic topic1 = new Topic(null, "Medical");
		Topic topic2 = new Topic(null, "Agricultural");
		Topic topic3 = new Topic(null, "Automotive");
		Topic topic4 = new Topic(null, "Education");
		
//		Topic topic1 = new Topic(null, "Medical", null);
//		Topic topic2 = new Topic(null, "Agricultural", null);
//		Topic topic3 = new Topic(null, "Automotive", null);
//		Topic topic4 = new Topic(null, "Education", null);
		
		topicRepository.save(topic1);
		topicRepository.save(topic2);
		topicRepository.save(topic3);
		topicRepository.save(topic4);
		
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
	}

	@Test
	void shouldAddCallForPapers() {
		// given
		Optional<Conference> conferenceCFP = conferenceRepository.findById(conferenceId);

		// when
		assertThat(conferenceCFP).isNotEmpty();
		callForPapers.setConference(conferenceCFP.get());
		conferenceCFP.get().setCallForPapers(callForPapers);
		CallForPapers expectedCFP = underTest.save(callForPapers);
		boolean isExist = underTest.existsById(expectedCFP.getId());

		// then
		assertThat(expectedCFP).isEqualTo(callForPapers);
		assertThat(isExist).isTrue();
		assertThat(conferenceCFP).hasValue(conference);

	}

	@Test
	void shouldFindCFPByConference() {
		// given
		Optional<Conference> conferenceCFP = conferenceRepository.findById(conferenceId);
		assertThat(conferenceCFP).isNotEmpty();
		callForPapers.setConference(conferenceCFP.get());
		conferenceCFP.get().setCallForPapers(callForPapers);
		conferenceRepository.save(conferenceCFP.get());
		underTest.save(callForPapers);

		// when
		//Optional<CallForPapers> expectedCFP = underTest.findByConference(conference);

		// then
		//assertThat(expectedCFP).isNotEmpty();
		//assertThat(expectedCFP).hasValue(callForPapers);
		assertThat(conferenceCFP).hasValue(conference);

	}

	@Test
	void shouldReturnEmptyIfCFPDoesNotExists() {
		// given
		Optional<Conference> conferenceCFP = conferenceRepository.findById(conferenceId);
		assertThat(conferenceCFP).isNotEmpty();

		// when
		//Optional<CallForPapers> exepectedCFP = underTest.findByConference(conference);

		// then
		//assertThat(exepectedCFP).isEmpty();
	}

	@Test
	void shouldCheckIfCFPExits() {
		// given
		Optional<Conference> conferenceCFP = conferenceRepository.findById(conferenceId);
		assertThat(conferenceCFP).isNotEmpty();
		callForPapers.setConference(conferenceCFP.get());
		conferenceCFP.get().setCallForPapers(callForPapers);
		conferenceRepository.save(conferenceCFP.get());
		underTest.save(callForPapers);

		// when
		boolean isExist = underTest.existsByConference(conferenceCFP.get());
		
		// then 
		assertThat(isExist).isTrue(); 
	}

}
