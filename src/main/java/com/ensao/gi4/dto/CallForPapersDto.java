package com.ensao.gi4.dto;

import java.time.LocalDate;
import java.util.Set;

public record  CallForPapersDto(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Set<String> topics,
        String guidelines) {

}