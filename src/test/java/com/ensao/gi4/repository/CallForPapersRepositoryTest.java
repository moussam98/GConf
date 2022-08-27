package com.ensao.gi4.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;

@DataJpaTest
public class CallForPapersRepositoryTest {

	@Autowired
	private CallForPapersRepository underTest;
	@Autowired
	private ConferenceRepository conferenceRepository;
	private Conference conference;
	private CallForPapers callForPapers;
	Long conferenceId;

	@BeforeEach
	void setUp() {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conferenceId = conferenceRepository.save(conference).getId();

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
		Optional<CallForPapers> expectedCFP = underTest.findByConference(conference);

		// then
		assertThat(expectedCFP).isNotEmpty();
		assertThat(expectedCFP).hasValue(callForPapers);
		assertThat(conferenceCFP).hasValue(conference);

	}

	@Test
	void shouldReturnEmptyIfCFPDoesNotExists() {
		// given
		Optional<Conference> conferenceCFP = conferenceRepository.findById(conferenceId);
		assertThat(conferenceCFP).isNotEmpty();

		// when
		Optional<CallForPapers> exepectedCFP = underTest.findByConference(conference);

		// then
		assertThat(exepectedCFP).isEmpty();
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
