package com.ensao.gi4.repository;

import com.ensao.gi4.model.Author;
import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.model.Submission;
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
	private DocumentRepository documentRepository;
	@Autowired
	private AuthorRepository authorRepository;
	private Conference conference;
	private List<Author> authors;
	private Set<String> keywords;
	private Document document;
	private Submission submission;

	@BeforeEach
	void setUp() {
        conference = new Conference(
                "International Conference",
                "IC",
                "UMP",
                "Oujda",
                "Morocco",
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                "Computer Science",
                "Artificial Intelligence",
                "organizeName");

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

		keywords = new HashSet<>();
		keywords.add("Artificial Intelligence");
		keywords.add("Internet Of Things");
        keywords.add("Block chain");
		keywords.add("Technology");

		document = new Document();
		document.setFilename("Test.pdf");
		document.setFileType(MediaType.APPLICATION_PDF_VALUE);
		document.setData("Hello world".getBytes());

		submission = new Submission(
                null,
                "Title of submission",
                "Description of subject",
                null,
                null,
                null,
                null,
                null,
				null,
				null);

	}

	@Test
	void shouldAddSubmission() {
		// given
		conferenceRepository.save(conference);
		authorRepository.saveAll(authors);
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

	@Test
	void shouldComputeSubmissionStatistics() {
		// given
		var document1 = new Document();
		document.setFilename("Test1.pdf");
		document.setFileType(MediaType.APPLICATION_PDF_VALUE);
		document.setData("Hello world".getBytes());
		var document2 = new Document();
		document.setFilename("Test2.pdf");
		document.setFileType(MediaType.APPLICATION_PDF_VALUE);
		document.setData("Hello world".getBytes());
		conferenceRepository.save(conference);
		authorRepository.saveAll(authors);
		documentRepository.save(document1);
		documentRepository.save(document2);

		submission.setConference(conference);
		submission.setAuthors(authors);
		submission.setKeywords(keywords);
		submission.setDocument(document1);
		submission.setIsEvaluate(true);
		submission.setIsValidate(true);
		underTest.save(submission);

		var sub2 = new Submission();
		sub2.setConference(conference);
		sub2.setAuthors(authors);
		sub2.setKeywords(keywords);
		sub2.setDocument(document2);
		sub2.setIsEvaluate(true);
		sub2.setIsValidate(false);
		underTest.save(sub2);

		// WHEN
		var result = underTest.findSubmissionStatisticsByConferenceId(conference.getId());

		// THEN
		assertThat(result).isNotNull();
		assertThat(result.totalSubmissions()).isEqualTo(2);
		assertThat(result.pendingCount()).isEqualTo(0);
		assertThat(result.acceptedCount()).isEqualTo(1);
		assertThat(result.rejectedCount()).isEqualTo(1);


	}

}
