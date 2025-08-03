package com.ensao.gi4.dto;

import java.util.List;
import java.util.Set;

public record SubmissionDto(
        Long id,
        String title,
        String description,
        Set<String> keywords,
        Long documentId,
        Long conferenceId,
        List<AuthorDto> authors,
        Boolean isEvaluate,
        Boolean isValidate) {
}
