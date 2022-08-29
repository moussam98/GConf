package com.ensao.gi4.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensao.gi4.model.Document;
import com.ensao.gi4.repository.DocumentRepository;
import com.ensao.gi4.service.api.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

	private DocumentRepository documentRepository;

	@Autowired
	public DocumentServiceImpl(DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}

	@Override
	public int add(Document document) {
		documentRepository.save(document);
		return 1;
	}

	@Override
	public Optional<Document> findById(Long fileId) {
		return documentRepository.findById(fileId);
	}

}
