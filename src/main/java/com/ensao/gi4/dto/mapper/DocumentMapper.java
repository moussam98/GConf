package com.ensao.gi4.dto.mapper;

import com.ensao.gi4.dto.DocumentMetadataDto;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.projection.DocumentProjection;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DocumentMapper {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    DocumentMetadataDto toDocumentMetadataDto(DocumentProjection documentProjection);
    DocumentMetadataDto toDocumentMetadataDto(Document document);
}
