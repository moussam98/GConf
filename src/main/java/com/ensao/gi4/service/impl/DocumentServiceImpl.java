package com.ensao.gi4.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ensao.gi4.model.Document;
import com.ensao.gi4.repository.DocumentRepository;
import com.ensao.gi4.service.api.DocumentService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

	private final DocumentRepository documentRepository;
	
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
