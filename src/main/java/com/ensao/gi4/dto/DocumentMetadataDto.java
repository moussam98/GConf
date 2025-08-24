package com.ensao.gi4.dto;

public record DocumentMetadataDto(
        Long id,
        String filename,
        String fileType,
        Long sizeInBytes) {
}
