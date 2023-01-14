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
	private Set<Topic> topics;

	@BeforeEach
	void setUp() {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");

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
		callForPapers.setStartDate(LocalDate.now());
		callForPapers.setEndDate(LocalDate.of(2022, 8, 30));
		callForPapers.setGuidelines("Guidelines instruction");
		callForPapers.setTopics(topics);
	}

	@Test
	void shouldAddCallForPapers() {
		// given
		conferenceRepository.save(conference);
		topicRepository.saveAll(topics);

		callForPapers.setConference(conference);
		callForPapers.setTopics(topics);

		// when
		CallForPapers savedCallForPapers = underTest.save(callForPapers);

		// then
		assertThat(savedCallForPapers).isEqualTo(callForPapers);

	}

	@Test
	void shouldFindCFPByConference() {
		// given
		conferenceRepository.save(conference);
		topicRepository.saveAll(topics);

		callForPapers.setConference(conference);
		callForPapers.setTopics(topics);
		underTest.save(callForPapers);

		// when
		Optional<CallForPapers> optionalCallForPapers = underTest.findByConference(conference);

		// then
		assertThat(optionalCallForPapers).isNotEmpty();
		assertThat(optionalCallForPapers).hasValue(callForPapers);

	}

	@Test
	void shouldReturnEmptyIfCFPDoesNotExists() {
		// given
		conferenceRepository.save(conference);

		// when
		Optional<CallForPapers> optionalCallForPapers = underTest.findByConference(conference);

		// then
		assertThat(optionalCallForPapers).isEmpty();
	}

	@Test
	void shouldCheckIfCFPExits() {
		// given
		conferenceRepository.save(conference);
		topicRepository.saveAll(topics);

		callForPapers.setConference(conference);
		callForPapers.setTopics(topics);
		underTest.save(callForPapers);

		// when
		boolean isExist = underTest.existsByConference(conference);

		// then
		assertThat(isExist).isTrue();
	}

}
