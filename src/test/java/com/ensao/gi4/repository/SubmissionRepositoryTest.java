package com.ensao.gi4.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;

import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.model.Keyword;
import com.ensao.gi4.model.Person;
import com.ensao.gi4.model.Submission;

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
	private Conference conference;
	private Person person1, person2, person3, person4;
	private Keyword keyword1, keyword2, keyword3, keyword4;
	private Document document;
	private Submission submission;

	@BeforeEach
	void setUp() {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");

//		person1 = new Person(null, "Ali1", "Moussa", "ali@gmail.com", "Morocco", "ENSAO", null);
//		person2 = new Person(null, "Ali2", "Moussa", "ali@gmail.com", "Morocco", "ENSAO", null);
//		person3 = new Person(null, "Ali3", "Moussa", "ali@gmail.com", "Morocco", "ENSAO", null);
//		person4 = new Person(null, "Ali4", "Moussa", "ali@gmail.com", "Morocco", "ENSAO", null);

		keyword1 = new Keyword(null, "Artificial Intelligence");
		keyword2 = new Keyword(null, "Internet Of Things");
		keyword3 = new Keyword(null, "Block chain");
		keyword4 = new Keyword(null, "Technologie");

		document = new Document();
		document = new Document();
		document.setFilename("Test.pdf");
		document.setFileType(MediaType.APPLICATION_PDF_VALUE);
		document.setData("Hello world".getBytes());

		submission = new Submission(null, "Subject", "Description of subject", null, null, null, null);

	}

	@Test
	void shouldAddSubmission() {
		// when
		conference = conferenceRepository.save(conference);

//		personRepository.save(person1);
//		personRepository.save(person2);
//		personRepository.save(person3);
//		personRepository.save(person4);

		keywordRepository.save(keyword1);
		keywordRepository.save(keyword2);
		keywordRepository.save(keyword3);
		keywordRepository.save(keyword4);

		documentRepository.save(document);

		Set<Keyword> keywords = new HashSet<>();
		keywords.add(keyword1);
		keywords.add(keyword2);
		keywords.add(keyword3);
		keywords.add(keyword4);

		List<Person> authors = new ArrayList<>();
		authors.add(person1);
		authors.add(person2);
		authors.add(person3);
		authors.add(person4);

		submission.setConference(conference);
		//submission.setAuthors(authors);
		submission.setKeywords(keywords);
		submission.setDocument(document);

		Submission expectedSubmission = underTest.save(submission);

		// then
		assertThat(expectedSubmission).isEqualTo(submission);
	}

	@Test
	void shouldFindAllSubmission() {
		// given
		conference = conferenceRepository.save(conference);

//		personRepository.save(person1);
//		personRepository.save(person2);
//		personRepository.save(person3);
//		personRepository.save(person4);

		keywordRepository.save(keyword1);
		keywordRepository.save(keyword2);
		keywordRepository.save(keyword3);
		keywordRepository.save(keyword4);

		documentRepository.save(document);

		Set<Keyword> keywords = new HashSet<>();
		keywords.add(keyword1);
		keywords.add(keyword2);
		keywords.add(keyword3);
		keywords.add(keyword4);

		List<Person> authors = new ArrayList<>();
		authors.add(person1);
		authors.add(person2);
		authors.add(person3);
		authors.add(person4);

		submission.setConference(conference);
		//submission.setAuthors(authors);
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
	void shouldFindDocumentById() {
		// given
		conference = conferenceRepository.save(conference);

//		personRepository.save(person1);
//		personRepository.save(person2);
//		personRepository.save(person3);
//		personRepository.save(person4);

		keywordRepository.save(keyword1);
		keywordRepository.save(keyword2);
		keywordRepository.save(keyword3);
		keywordRepository.save(keyword4);

		documentRepository.save(document);

		Set<Keyword> keywords = new HashSet<>();
		keywords.add(keyword1);
		keywords.add(keyword2);
		keywords.add(keyword3);
		keywords.add(keyword4);

		List<Person> authors = new ArrayList<>();
		authors.add(person1);
		authors.add(person2);
		authors.add(person3);
		authors.add(person4);

		submission.setConference(conference);
		//submission.setAuthors(authors);
		submission.setKeywords(keywords);
		submission.setDocument(document);

		Submission savedSubmission = underTest.save(submission);
		
		// when
		Optional<Submission> submissionOptional = underTest.findById(savedSubmission.getId());
		
		// then 
		assertThat(submissionOptional).isNotEmpty(); 
		assertThat(submissionOptional).hasValue(savedSubmission); 
	}

}
