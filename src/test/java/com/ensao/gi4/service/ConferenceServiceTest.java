package com.ensao.gi4.service;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferencePatchDto;
import com.ensao.gi4.dto.ConferenceRequestDto;
import com.ensao.gi4.dto.UserDto;
import com.ensao.gi4.dto.mapper.CallForPapersMapper;
import com.ensao.gi4.dto.mapper.ConferenceMapper;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Role;
import com.ensao.gi4.model.User;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.service.api.UserService;
import com.ensao.gi4.service.impl.ConferenceServiceImpl;
import com.ensao.gi4.utils.MessageSourceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConferenceServiceTest {

	@Mock
	private ConferenceRepository conferenceRepository;
	@Mock
	private UserService userService;
	@Mock
	private MessageSourceUtils messageSourceUtils;
	private ConferenceService underTest;
	private Conference conference;
	private User owner;
	private ConferenceRequestDto conferenceRequestDto;
	private CallForPapers callForPapers;

    @BeforeEach
	void setUp() {
		underTest = new ConferenceServiceImpl(conferenceRepository, userService, messageSourceUtils,
				ConferenceMapper.INSTANCE);
        conferenceRequestDto = createConferenceRequestDto();
		conference = createConference();
        owner = createUser();
		conference.setOwner(owner);
		callForPapers = createCallForPapers();
		conference.setCallForPapers(callForPapers);
	}

	@Test
	void shouldAddConference() {
		// given
		Long userId = 1L;
		Conference conference = ConferenceMapper.INSTANCE.toConference(conferenceRequestDto, userId);

		Conference savedconference = new Conference();
		savedconference.setName("International Conference");
		savedconference.setAcronym("GConf");
		savedconference.setId(1L);
		savedconference.setOwner(owner);

		// when
		when(conferenceRepository.existsByNameAndAcronym(conference.getName(),conference.getAcronym()))
				.thenReturn(false);
		when(userService.getById(userId)).thenReturn(createUserDto());
		when(conferenceRepository.save(any())).thenReturn(savedconference);

		ArgumentCaptor<String> currentStringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		var acronymArgumentCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

		ConferenceDto result = underTest.add(conferenceRequestDto, owner.getId());

		// then
		assertThat(result.id()).isEqualTo(savedconference.getId());
		assertThat(result.name()).isEqualTo(savedconference.getName());
		assertThat(result.acronym()).isEqualTo(savedconference.getAcronym());

		verify(conferenceRepository, times(1)).existsByNameAndAcronym(
				currentStringArgumentCaptor.capture(), acronymArgumentCaptor.capture());
		assertThat(currentStringArgumentCaptor.getValue()).isEqualTo(conference.getName());
		assertThat(acronymArgumentCaptor.getValue()).isEqualTo(conference.getAcronym());

		verify(userService, times(1)).getById(userIdArgumentCaptor.capture());
		assertThat(userIdArgumentCaptor.getValue()).isEqualTo(owner.getId());
	}

	@Test
	void shouldNotAddConferenceIfExists() {
		// when
		when(conferenceRepository.existsByNameAndAcronym(conference.getName(),conference.getAcronym())).thenReturn(true);

		// then
		assertThatThrownBy(() -> underTest.add(conferenceRequestDto, owner.getId()))
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void shouldReturnConferenceIfExists() {
        // Given
        var conferenceDto = ConferenceMapper.INSTANCE.toConferenceDto(conference);
		var conferenceProjection = new ConferenceProjectionImpl(conference);
        // when
        Long conferenceId = 1L;
		when(conferenceRepository.findConferenceById(conferenceId)).thenReturn(Optional.of(conferenceProjection));
		Optional<ConferenceDto> optionalConference = underTest.findById(conferenceId);
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

		// then
		assertThat(optionalConference).isNotEmpty();
		assertThat(optionalConference).hasValue(conferenceDto);
		verify(conferenceRepository, times(1)).findConferenceById(conferenceIdArgumentCaptor.capture());
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conference.getId());
	}

	@Test
	void shouldReturnEmptyIfConferenceDoesNotExists() {
		// when
        Long conferenceId = 1L;
		when(conferenceRepository.findConferenceById(conferenceId)).thenReturn(Optional.empty());
		Optional<ConferenceDto> expectedConference = underTest.findById(conferenceId);
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

		// then
		assertThat(expectedConference).isEmpty();
		verify(conferenceRepository, times(1)).findConferenceById(conferenceIdArgumentCaptor.capture());
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conference.getId());
	}

	@Test
	void shouldUpdateConferenceById() {
        // Given
        var conferencePatchDto = createConferencePatchDto();
		Long conferenceId = 1L;
        // when
		when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(conference));
		when(conferenceRepository.save(conference)).thenReturn(conference);
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);

		Optional<ConferenceDto> optionalConference = underTest.updateConferenceById(conferenceId, conferencePatchDto);

		// then
		verify(conferenceRepository, times(1)).findById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId());
		assertThat(optionalConference).isNotEmpty();
		assertThat(optionalConference)
				.isPresent()
				.hasValueSatisfying( conf -> {
					assertThat(conf.name()).isEqualTo(conference.getName());
					assertThat(conf.city()).isEqualTo(conference.getCity());
					assertThat(conf.callForPapers()).isEqualTo(CallForPapersMapper.INSTANCE
							.toCallForPapersDto(conference.getCallForPapers()));
				});
	}

    @Test
	void shouldNoUpdateConferenceIfDoesNotExists() {
        // Given
        var conferencePatchDto =  createConferencePatchDto();
		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.empty());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		Optional<ConferenceDto> optionalConference = underTest.updateConferenceById(conference.getId(), conferencePatchDto);

		// then
		verify(conferenceRepository, times(1)).findById(idArgumentCaptor.capture());
		assertThat(idArgumentCaptor.getValue()).isEqualTo(conference.getId());
		assertThat(optionalConference).isEmpty();
	}

	private CallForPapers createCallForPapers() {
		var callForPapers  =new CallForPapers();
		callForPapers.setConference(conference);
		callForPapers.setId(1L);
		callForPapers.setStartDate(LocalDate.now());
		callForPapers.setEndDate(LocalDate.now());
		callForPapers.setGuidelines("Call for papers guidelines");
		callForPapers.setTopics(Set.of("Machine learning"));
		return callForPapers;
	}

    private User createUser() {
        owner = new User();
        owner.setId(1L);
        owner.setFirstName("Ali");
        owner.setLastName("Moussa");
        owner.setEmail("ali@gmail.com");
        owner.setRole(Role.ADMIN);
        return owner;
    }

    private UserDto createUserDto() {
        return new UserDto(1L, "Ali", "Moussa", "ali@gmail.com", Role.ADMIN,
				Instant.now(), Instant.now());
    }

    private Conference createConference() {
        var conference =  new Conference(
                "International Conference",
                "GConf",
                "UMP",
                "Oujda",
                "Morocco",
                LocalDate.now(),
                LocalDate.of(2022, 8, 30),
                "Computer Science",
                "Artificial Intelligence",
                "organizeName");
        conference.setId(1L);
		conference.setCreatedAt(Instant.now());
		conference.setUpdatedAt(Instant.now());
		conference.setCallForPapers(callForPapers);
        return conference;
    }


    private static ConferenceRequestDto createConferenceRequestDto() {
        return new ConferenceRequestDto(
                "International Conference",
                "GConf",
                "UMP",
                "Oujda",
                "Morocco",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                "Computer Science",
                "Artificial Intelligence",
                "organizeName",
                "+55121131",
                "Other info"
        );
    }

    private static ConferencePatchDto createConferencePatchDto() {
        return new ConferencePatchDto(
                "UMP",
                "Oujda",
                "Morocco",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                "Computer Science",
                "Artificial Intelligence",
                "organizeName",
                "+55121131",
                "Other info"
        );
    }

}
