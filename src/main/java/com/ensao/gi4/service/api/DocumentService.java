package com.ensao.gi4.service.api;

import com.ensao.gi4.model.Document;

import java.util.Optional;

public interface DocumentService {
	
	int add(Document document);
	Optional<Document> findById(Long fileId);
    Boolean deleteById(Long id);
}
