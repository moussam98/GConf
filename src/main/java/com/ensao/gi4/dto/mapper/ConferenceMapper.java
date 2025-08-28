package com.ensao.gi4.dto.mapper;

import com.ensao.gi4.dto.ConferenceDto;
import com.ensao.gi4.dto.ConferenceRequestDto;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.projection.ConferenceProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS)
public interface ConferenceMapper {

    ConferenceMapper INSTANCE = Mappers.getMapper(ConferenceMapper.class);

    ConferenceDto toConferenceDto(Conference conference);
    ConferenceDto toConferenceDto(ConferenceProjection conferenceProjection);
    @Mapping(target = "submissions",  ignore = true)
    @Mapping(target = "owner.id", source = "userId")
    Conference toConference(ConferenceRequestDto conferenceRequestDto, Long userId);
}
