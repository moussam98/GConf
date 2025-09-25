package com.ensao.gi4.dto.mapper;

import com.ensao.gi4.dto.SubmissionDto;
import com.ensao.gi4.dto.SubmissionRequestDto;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.projection.SubmissionProjection;
import com.ensao.gi4.service.exception.DocumentProcessingException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS)
public interface SubmissionMapper {

    @Mapping(target = "conference.id", source = "conferenceId")
    @Mapping(target = "document.filename", source = "submissionRequestDto.document.originalFilename")
    @Mapping(target = "document.fileType", source = "submissionRequestDto.document.contentType")
    @Mapping(target = "document.sizeInBytes", source = "submissionRequestDto.document.size")
    @Mapping(target = "document.data", expression = "java(getDocumentBytes(multipartFile))")
    @Mapping(target = "createdAt", expression = "java(LocalDate.now())")
    Submission toSubmission(SubmissionRequestDto submissionRequestDto, Long conferenceId);
    SubmissionDto toSubmissionDto(SubmissionProjection submissionProjection);
    SubmissionDto toSubmissionDto(Submission submission);


    default byte[] getDocumentBytes(MultipartFile document) {
        if (document == null || document.isEmpty()) {
            return null;
        }
        try {
            return document.getBytes();
        } catch (IOException e) {
            throw new DocumentProcessingException("Failed to read document bytes", e);
        }
    }
}
