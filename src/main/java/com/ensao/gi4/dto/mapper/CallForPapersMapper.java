package com.ensao.gi4.dto.mapper;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.dto.CallForPapersRequestDto;
import com.ensao.gi4.model.CallForPapers;
import com.ensao.gi4.projection.CallForPapersProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS)
public interface CallForPapersMapper {

    CallForPapersMapper INSTANCE = Mappers.getMapper(CallForPapersMapper.class);

    CallForPapersDto toCallForPapersDto(CallForPapers callForPapers);
    CallForPapersDto toCallForPapersDto(CallForPapersProjection callForPapersProjection);
    @Mapping(target = "conference.id", source = "conferenceId")
    CallForPapers toCallForPapers(CallForPapersRequestDto callForPapersRequestDto, Long conferenceId);
}
