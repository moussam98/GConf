package com.ensao.gi4.service.impl;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferencePatchDto;
import com.ensao.gi4.dto.ConferenceRequestDto;
import com.ensao.gi4.dto.mapper.ConferenceMapper;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.User;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.service.api.ConferenceService;
import com.ensao.gi4.service.api.UserService;
import com.ensao.gi4.utils.MessageSourceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

	private final ConferenceRepository conferenceRepository;
    private final UserService userService;
	private final MessageSourceUtils messageSourceUtils;
	private final ConferenceMapper conferenceMapper;

	@Override
	public ConferenceDto add(ConferenceRequestDto conferenceRequestDto, Long userId) {

		if (conferenceRepository.existsByNameAndAcronym(conferenceRequestDto.name(), conferenceRequestDto.acronym())) {
			throw new IllegalStateException(messageSourceUtils
					.getMessage(
							"organizer.conference.conflict",
							new Object[]{conferenceRequestDto.name(), conferenceRequestDto.acronym()},
							Locale.ENGLISH));
		} else {
            Conference conference = conferenceMapper.toConference(conferenceRequestDto, userService.getById(userId).id());
			Instant instant = Instant.now();
			conference.setCreatedAt(instant);
			conference.setUpdatedAt(instant);
			return conferenceMapper.toConferenceDto(conferenceRepository.save(conference));
		}
	}

	@Override
	public Optional<ConferenceDto> findById(Long id) {
		return conferenceRepository.findConferenceById(id)
				.map(conferenceMapper::toConferenceDto);
	}

	@Override
	public Optional<ConferenceDto> updateConferenceById(Long id, ConferencePatchDto conferencePatchDto) {
		return conferenceRepository.findById(id)
				.map(existingConference -> {
					updateConferenceFields(existingConference, conferencePatchDto);
					Conference savedConference = conferenceRepository.save(existingConference);
					return conferenceMapper.toConferenceDto(savedConference);
				});
    }

	@Override
	@Transactional
	public void deleteById(Long id) {
		conferenceRepository.deleteById(id);
	}
	
	private void updateConferenceFields(Conference target, ConferencePatchDto source) {
		updateField(source.venue(),  target::setVenue);
		updateField(source.city(), target::setCity);
		updateField(source.country(), target::setCountry);
		updateField(source.startDate(), target::setStartDate);
		updateField(source.endDate(), target::setEndDate);
		updateField(source.primaryArea(),  target::setPrimaryArea);
		updateField(source.secondaryArea(),  target::setSecondaryArea);
		updateField(source.organizer(),   target::setOrganizer);
		updateField(source.phoneNumber(),  target::setPhoneNumber);
		updateField(source.otherInfo(), target::setOtherInfo);
		target.setUpdatedAt(Instant.now());
	}

	private <T> void updateField(T value, Consumer<T> setter){
		if(value != null){
			setter.accept(value);
		}
	}

	@Override
	public Optional<ConferenceDto> findByOwnerId(Long ownerId) {
		var owner = new User();
		owner.setId(ownerId);
		return conferenceRepository.findByOwner(owner)
				.map(conferenceMapper::toConferenceDto);
	}

}
