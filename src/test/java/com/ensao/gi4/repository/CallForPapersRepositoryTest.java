//package com.ensao.gi4.repository;
//
//import com.ensao.gi4.model.CallForPapers;
//import com.ensao.gi4.model.Conference;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class CallForPapersRepositoryTest {
//
//	@Autowired
//	private CallForPapersRepository underTest;
//	@Autowired
//	private ConferenceRepository conferenceRepository;
//	private Conference conference;
//	private CallForPapers callForPapers;
//	private Set<String> topics;
//
//	@BeforeEach
//	void setUp() {
//		conference = new Conference(
//                "International Conference",
//                "IC",
//                "UMP",
//                "Oujda",
//                "Morrocco",
//                LocalDate.now(),
//				LocalDate.now().plusMonths(1),
//                "Computer Science",
//                "Artificial Intelligence",
//                "organizeName");
//
//		topics = new HashSet<>();
//		topics.add("Medical");
//		topics.add("Agricultural");
//		topics.add("Automotive");
//		topics.add("Education");
//
//		callForPapers = new CallForPapers();
//		callForPapers.setStartDate(LocalDate.now());
//		callForPapers.setEndDate(LocalDate.of(2022, 8, 30));
//		callForPapers.setGuidelines("Guidelines instruction");
//		callForPapers.setTopics(topics);
//	}
//
//	@Test
//	void shouldAddCallForPapers() {
//		// given
//		conferenceRepository.save(conference);
//
//		callForPapers.setConference(conference);
//		callForPapers.setTopics(topics);
//
//		// when
//		CallForPapers savedCallForPapers = underTest.save(callForPapers);
//
//		// then
//		assertThat(savedCallForPapers).isEqualTo(callForPapers);
//
//	}
//
//	@Test
//	void shouldFindCFPByConference() {
//		// given
//		conferenceRepository.save(conference);
//
//		callForPapers.setConference(conference);
//		callForPapers.setTopics(topics);
//		underTest.save(callForPapers);
//
//		// when
//		Optional<CallForPapers> optionalCallForPapers = underTest.findByConference(conference);
//
//		// then
//		assertThat(optionalCallForPapers).isNotEmpty();
//		assertThat(optionalCallForPapers).hasValue(callForPapers);
//
//	}
//
//	@Test
//	void shouldReturnEmptyIfCFPDoesNotExists() {
//		// given
//		conferenceRepository.save(conference);
//
//		// when
//		Optional<CallForPapers> optionalCallForPapers = underTest.findByConference(conference);
//
//		// then
//		assertThat(optionalCallForPapers).isEmpty();
//	}
//
//	@Test
//	void shouldCheckIfCFPExits() {
//		// given
//		conferenceRepository.save(conference);
//
//		callForPapers.setConference(conference);
//		callForPapers.setTopics(topics);
//		underTest.save(callForPapers);
//
//		// when
//		boolean isExist = underTest.existsByConference(conference);
//
//		// then
//		assertThat(isExist).isTrue();
//	}
//
//}
