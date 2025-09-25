package com.ensao.gi4.dto;

public record SubmissionStatisticsDto(
        long totalSubmissions,
        long acceptedCount,
        long pendingCount,
        long rejectedCount
) {}