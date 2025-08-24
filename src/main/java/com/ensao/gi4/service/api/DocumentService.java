package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.DocumentMetadataDto;
import com.ensao.gi4.model.Document;

import java.util.Optional;

public interface DocumentService {
	
	DocumentMetadataDto add(Document document);
	Optional<Document> findById(Long id);
    Boolean deleteById(Long id);
	Optional<DocumentMetadataDto> findDocumentMetadataById(Long documentId);
}
