package com.ensao.gi4.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ensao.gi4.model.Conference;

@DataJpaTest
public class ConferenceRepositoryTest {

	@Autowired
	private ConferenceRepository underTest;
	private Conference conference;

	@BeforeEach
	void setUp() {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
	}

	@Test
	void shouldAddConference() {
		// when
		Conference expectedConference = underTest.save(conference);

		// then
		assertThat(expectedConference).isEqualTo(conference);
	}

	@Test
	void shouldCheckIfConferenceExists() {
		// given
		underTest.save(conference);

		// when
		boolean exist = underTest.existsByName(conference.getName());

		// then
		assertThat(exist).isTrue();

	}

	@Test
	void shouldReturnConferenceIfExist() {
		// given
		underTest.save(conference);

		// when
		//Optional<Conference> expectedConference = underTest.findByName(conference.getName());

		// then
//		assertThat(expectedConference).isNotEmpty();
//		assertThat(expectedConference).hasValue(conference);
	}

	@Test
	void shouldReturnEmptyIfConferenceNameDoesNotExists() {
		// when
		//Optional<Conference> expectedConference = underTest.findByName(conference.getName());

		// then
	//	assertThat(expectedConference).isEmpty();
	}

	@Test
	void shouldTestIfConferenceExistByAcronymName() {
		// given 
		underTest.save(conference); 
		
		// when 
		boolean exists = underTest.existsByAcronym(conference.getAcronym());
		
		// then 
		assertThat(exists).isTrue(); 
	}

}
