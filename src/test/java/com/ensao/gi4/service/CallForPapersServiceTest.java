package com.ensao.gi4.service;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.dto.CallForPapersRequestDto;
import com.ensao.gi4.dto.mapper.Mapper;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Role;
import com.ensao.gi4.model.User;
import com.ensao.gi4.repository.CallForPapersRepository;
import com.ensao.gi4.service.api.CallForPapersService;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.service.impl.CallForPapersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CallForPapersServiceTest {

	@Mock
	private ConferenceService conferenceService;
	@Mock
	private CallForPapersRepository callForPapersRepository;
	private CallForPapersService underTest;
	Conference conference;
	CallForPapers callForPapers;
	private Set<String> topics;
    private CallForPapersRequestDto callForPapersRequestDto;

	@BeforeEach
	void setUp() {
		underTest = new CallForPapersServiceImpl(callForPapersRepository, conferenceService);

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
        conference.setOwner(createUser());

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

        callForPapersRequestDto = createCallForPapersDto("10/04/2022", "30/04/2022");
	}

    private User createUser() {
        var owner = new User();
        owner.setId(1L);
        owner.setFirstName("Ali");
        owner.setLastName("Moussa");
        owner.setEmail("ali@gmail.com");
        owner.setRole(Role.ADMIN);
        return owner;
    }
	@Test
	void shouldAddCFP() {
        // Given
		// when
		when(conferenceService.findById(conference.getId()))
                .thenReturn(Optional.of(Mapper.toConferenceDto(conference)));
		when(callForPapersRepository.save(any())).thenReturn(callForPapers);

		Optional<CallForPapersDto> optionalCFP = underTest.add(callForPapersRequestDto, conference.getId());

		// then
		assertThat(optionalCFP)
                .isPresent()
                .hasValueSatisfying( cfp -> {
					assertThat(cfp.guidelines()).isEqualTo(callForPapers.getGuidelines());
                    assertThat(cfp.startDate()).isEqualTo(callForPapers.getStartDate());
                    assertThat(cfp.endDate()).isEqualTo(callForPapers.getEndDate());
                    assertThat(cfp.topics()).hasSameSizeAs(topics);
                });
	}

	@Test
	void shouldNotAddCFP() {
		// given
        var callForPapersDto = createCallForPapersDto("10/04/2022", "30/04/2022");

        // when
		when(conferenceService.findById(conference.getId())).thenReturn(Optional.empty());
		Optional<CallForPapersDto> optionalCFP = underTest.add(callForPapersDto, conference.getId());

		// then
		assertThat(optionalCFP).isEmpty();

	}

    private CallForPapersRequestDto createCallForPapersDto(String startDate, String endDate) {
        var callForPapersDto = new CallForPapersRequestDto(
                startDate,
                endDate,
                topics,
                "Guidelines instruction");
        callForPapers.setTopics(topics);
        callForPapers.setConference(conference);
        return callForPapersDto;
    }

    @Test
	void shouldFindCFPByConferenceId() {
        // Given
        Long conferenceId = 1L;
        var callForPapersProjection = new CallForPapersProjectionImpl(callForPapers);

		// when
		when(callForPapersRepository.findByConference(any()))
                .thenReturn(Optional.of(callForPapersProjection));

		Optional<CallForPapersDto> optionalCFP = underTest.findByConferenceId(conference.getId());

		// then
        assertThat(optionalCFP)
                .isPresent()
                .hasValueSatisfying( cfp -> {
                    assertThat(cfp.guidelines()).isEqualTo(callForPapers.getGuidelines());
                    assertThat(cfp.startDate()).isEqualTo(callForPapers.getStartDate());
                    assertThat(cfp.endDate()).isEqualTo(callForPapers.getEndDate());
                    assertThat(cfp.topics()).hasSameSizeAs(topics);
                });
	}

}
