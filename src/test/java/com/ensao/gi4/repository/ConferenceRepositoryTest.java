package com.ensao.gi4.repository;

import com.ensao.gi4.model.Conference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ConferenceRepositoryTest {

	@Autowired
	private ConferenceRepository underTest;
	private Conference conference;

	@BeforeEach
	void setUp() {
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
	}

	@Test
	void shouldAddConference() {
		// when
		Conference actualConference = underTest.save(conference);

		// then
		assertThat(actualConference).isEqualTo(conference);
	}

	@Test
	void shouldCheckIfConferenceExists() {
		// given
		underTest.save(conference);

		// when
		boolean exists = underTest.existsByNameAndAcronym(conference.getName(), conference.getAcronym());

		// then
		assertThat(exists).isTrue();

	}

	@Test
	void shouldReturnConferenceIfExist() {
		// given
        Conference savedConference = underTest.save(conference);

        // when
		Optional<Conference> optionalConference = underTest.findById(savedConference.getId());

		// then
		assertThat(optionalConference).isNotEmpty();
		assertThat(optionalConference).hasValue(conference);
	}

	@Test
	void shouldReturnEmptyIfConferenceNameDoesNotExists() {
		// when
		Optional<Conference> optionalConference = underTest.findById(Long.MIN_VALUE);

		// then
		assertThat(optionalConference).isEmpty();
	}

	@Test
	void shouldFindConferenceById() {
		underTest.save(conference);

		// when
		Optional<Conference> optionalConference = underTest.findById(conference.getId());

		// then
		assertThat(optionalConference).isNotEmpty();
		assertThat(optionalConference).hasValue(conference);
	}



}
