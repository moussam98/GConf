package com.ensao.gi4.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record SubmissionDto(
        Long id,
        String title,
        String description,
        Set<String> keywords,
        DocumentMetadataDto document,
        List<AuthorDto> authors,
        Boolean isEvaluate,
        Boolean isValidate,
        LocalDate createdAt) {
}
