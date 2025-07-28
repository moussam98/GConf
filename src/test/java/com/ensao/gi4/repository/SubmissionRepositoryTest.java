package com.ensao.gi4.repository;

import com.ensao.gi4.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SubmissionRepositoryTest {

	@Autowired
	private SubmissionRepository underTest;

	@Autowired
	private ConferenceRepository conferenceRepository;
	@Autowired
	private KeywordRepository keywordRepository;
	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private AuthorRepository authorRepository;
	private Conference conference;
	private List<Author> authors;
	private Set<Keyword> keywords;
	private Document document;
	private Submission submission;

	@BeforeEach
	void setUp() {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");

		Author author = new Author();
		Author author2 = new Author();
		author.setFirstName("Ali");
		author.setLastName("Moussa");
		author.setEmail("ali@gmail.com");
		author.setCountry("Niger");
		author.setOrganization("EMIG");

		author2.setFirstName("Anna");
		author2.setLastName("Smith");
		author2.setEmail("anna@gmail.com");
		author2.setCountry("United States");
		author2.setOrganization("Silicon Valley");

		authors = new ArrayList<>();
		authors.add(author);
		authors.add(author2);

		Keyword keyword1 = new Keyword(null, "Artificial Intelligence");
		Keyword keyword2 = new Keyword(null, "Internet Of Things");
		Keyword keyword3 = new Keyword(null, "Block chain");
		Keyword keyword4 = new Keyword(null, "Technologie");

		keywords = new HashSet<>();
		keywords.add(keyword1);
		keywords.add(keyword2);
		keywords.add(keyword3);
		keywords.add(keyword4);

		document = new Document();
		document.setFilename("Test.pdf");
		document.setFileType(MediaType.APPLICATION_PDF_VALUE);
		document.setData("Hello world".getBytes());

		submission = new Submission(null, "Title of submission", "Description of subject", null, null, null, null, null,
				null);

	}

	@Test
	void shouldAddSubmission() {
		// given
		conferenceRepository.save(conference);
		authorRepository.saveAll(authors);
		keywordRepository.saveAll(keywords);
		documentRepository.save(document);

		submission.setConference(conference);
		submission.setAuthors(authors);
		submission.setKeywords(keywords);
		submission.setDocument(document);

		// when
		Submission actualSubmission = underTest.save(submission);

		// then
		assertThat(actualSubmission).isEqualTo(submission);
	}

	@Test
	void shouldFindAllSubmission() {
		// given
		conferenceRepository.save(conference);
		authorRepository.saveAll(authors);
		keywordRepository.saveAll(keywords);
		documentRepository.save(document);

		submission.setConference(conference);
		submission.setAuthors(authors);
		submission.setKeywords(keywords);
		submission.setDocument(document);

		underTest.save(submission);

		// when
		List<Submission> submissionList = underTest.findAll();

		// then
		assertThat(submissionList).isNotEmpty();
		assertThat(submissionList).contains(submission);
		assertThat(submissionList.size()).isEqualTo(1);

	}

	@Test
	void shouldFindConferenceById() {
		// given
		conferenceRepository.save(conference);
		authorRepository.saveAll(authors);
		keywordRepository.saveAll(keywords);
		documentRepository.save(document);

		submission.setConference(conference);
		submission.setAuthors(authors);
		submission.setKeywords(keywords);
		submission.setDocument(document);

		underTest.save(submission);

		// when
		Optional<Submission> submissionOptional = underTest.findById(submission.getId());

		// then
		assertThat(submissionOptional).isNotEmpty();
		assertThat(submissionOptional).hasValue(submission);
	}

}
