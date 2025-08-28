package com.ensao.gi4.service.impl;

import com.ensao.gi4.dto.DocumentMetadataDto;
import com.ensao.gi4.dto.mapper.DocumentMapper;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.repository.DocumentRepository;
import com.ensao.gi4.service.api.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

	private final DocumentRepository documentRepository;
	private final DocumentMapper documentMapper;
	
	@Override
	public DocumentMetadataDto add(Document document) {
		return documentMapper.toDocumentMetadataDto(documentRepository.save(document));
	}

	@Override
	public Optional<Document> findById(Long id) {
		return documentRepository.findById(id);
	}

	@Override
	public Optional<DocumentMetadataDto> findDocumentMetadataById(Long documentId) {
		return documentRepository.findDocumentMetadataById(documentId)
				.map(documentMapper::toDocumentMetadataDto);
	}

	@Override
	public Boolean deleteById(Long id) {
		documentRepository.deleteById(id);
		return true;
	}

}
