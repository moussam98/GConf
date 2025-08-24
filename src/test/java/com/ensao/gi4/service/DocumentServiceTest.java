package com.ensao.gi4.service;

import com.ensao.gi4.model.Document;
import com.ensao.gi4.repository.DocumentRepository;
import com.ensao.gi4.service.api.DocumentService;
import com.ensao.gi4.service.impl.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

	@Mock
	private DocumentRepository documentRepository;
	private DocumentService underTest;
	private Document document;

	@BeforeEach
	void setUp() {
		underTest = new DocumentServiceImpl(documentRepository);
		document = new Document();
		document.setFilename("Test.pdf");
		document.setFileType(MediaType.APPLICATION_PDF_VALUE);
		document.setData("Hello world".getBytes());
		document.setId(1L);
	}

	@Test
	void shouldAddDocument() {
		// Given
		when(documentRepository.save(any())).thenReturn(document);
		// when 
		var actualResult = underTest.add(document);
		ArgumentCaptor<Document> documentArgumentCaptor = ArgumentCaptor.forClass(Document.class); 
		
		// then 
		assertThat(actualResult.filename()).isEqualTo(document.getFilename());
		assertThat(actualResult.fileType()).isEqualTo(document.getFileType());
		verify(documentRepository).save(documentArgumentCaptor.capture());
		assertThat(documentArgumentCaptor.getValue()).isEqualTo(document); 
	}
	
	@Test
	void shouldFindDocumentById() {
		// Given
		long documentId = 1L;
		// when
		when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
		Optional<Document> documentOptional = underTest.findById(documentId);
		
		// then 
		assertThat(documentOptional).isNotEmpty(); 
		assertThat(documentOptional).hasValue(document);
	}

}
