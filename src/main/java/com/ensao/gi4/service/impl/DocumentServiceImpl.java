package com.ensao.gi4.service.impl;

import com.ensao.gi4.model.Document;
import com.ensao.gi4.repository.DocumentRepository;
import com.ensao.gi4.service.api.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

	@Override
	public Boolean deleteById(Long id) {
		documentRepository.deleteById(id);
		return true;
	}

}
