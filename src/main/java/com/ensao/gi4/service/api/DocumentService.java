package com.ensao.gi4.service.api;

import java.util.Optional;

import com.ensao.gi4.model.Document;

public interface DocumentService {
	
	int add(Document document);
	Optional<Document> findById(Long fileId);

}
