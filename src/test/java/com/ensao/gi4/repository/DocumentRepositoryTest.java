package com.ensao.gi4.repository;

import com.ensao.gi4.model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DocumentRepositoryTest {

	@Autowired
	private DocumentRepository underTest;
	private Document document;

	@BeforeEach
	void setUp() {
		document = new Document();
		document.setFilename("Test.pdf");
		document.setFileType(MediaType.APPLICATION_PDF_VALUE);
		document.setData("Hello world".getBytes());
	}

	@Test
	void shouldAddDocument() {
		// when
		Document expectedDocument = underTest.save(document);

		// then
		assertThat(expectedDocument).isEqualTo(document);
	}

	@Test
	void shouldFindDocumentById() {
		// given
		underTest.save(document);
		Long documentId = document.getId();

		// when
		Optional<Document> optionalDocument = underTest.findById(documentId);

		// then
		assertThat(optionalDocument).isNotEmpty();
		assertThat(optionalDocument).hasValue(document);

	}
}
