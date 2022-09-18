package com.ensao.gi4.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import com.ensao.gi4.model.Conference;
import com.ensao.gi4.model.Document;
import com.ensao.gi4.model.Keyword;
import com.ensao.gi4.model.Person;
import com.ensao.gi4.model.Submission;
import com.ensao.gi4.repository.ConferenceRepository;
import com.ensao.gi4.repository.DocumentRepository;
import com.ensao.gi4.repository.KeywordRepository;
import com.ensao.gi4.repository.SubmissionRepository;
import com.ensao.gi4.service.impl.SubmissionServiceImpl;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SubmissionServiceTest {

	@Mock
	private SubmissionRepository submissionRepository;
	@Mock
	private ConferenceRepository conferenceRepository;
	@Mock
	private KeywordRepository keywordRepository;
	@Mock
	private DocumentRepository documentRepository; 
	
	@InjectMocks
	private SubmissionServiceImpl underTest;
	private Conference conference;
	private Person person1, person2, person3, person4;
	private Keyword keyword1, keyword2, keyword3, keyword4;
	private Document document;
	private Submission submission;
	Set<Keyword> keywords;
	List<Person> authors;

	@BeforeEach
	void setUp() {
		conference = new Conference("International Confernce", "IC", "UMP", "Oujda", "Morrocco", LocalDate.now(),
				LocalDate.of(2022, 8, 30), "Computer Science", "Artificial Intelligence", "organizeName");
		conference.setId(1l);

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

		keywords = new HashSet<>();
		keywords.add(keyword1);
		keywords.add(keyword2);
		keywords.add(keyword3);
		keywords.add(keyword4);

		authors = new ArrayList<>();
		authors.add(person1);
		authors.add(person2);
		authors.add(person3);
		authors.add(person4);

		submission = new Submission(null, "Subject", "Description of subject", null, null, null, null);

		submission.setConference(conference);
		//submission.setAuthors(authors);
		submission.setKeywords(keywords);
		submission.setDocument(document);
		submission.setId(1l);
	}

	@Test
	void shouldAddSubmission() {
		// given 
		List<Person> authors = Arrays.asList(person1, person2, person3, person4); 
	
		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.of(conference));
		when(submissionRepository.save(submission)).thenReturn(submission);
		//Long actualResult = underTest.add(submission, conference.getId());
		ArgumentCaptor<Submission> submissionArgumentCaptor = ArgumentCaptor.forClass(Submission.class);
		ArgumentCaptor<Long> conferenceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);  
		ArgumentCaptor<Document> documentArgumentCaptor = ArgumentCaptor.forClass(Document.class); 

		// then
		//assertThat(actualResult).isEqualTo(submission.getId());
		
		verify(submissionRepository).save(submissionArgumentCaptor.capture());
		assertThat(submissionArgumentCaptor.getValue()).isEqualTo(submission);
		
		verify(conferenceRepository).findById(conferenceIdArgumentCaptor.capture());
		assertThat(conferenceIdArgumentCaptor.getValue()).isEqualTo(conference.getId());
		
		//verify(personRepository, times(authors.size())).save(personArgumentCaptor.capture()); 
		assertThat(authors.contains(personArgumentCaptor.getValue())).isTrue(); 
		
		verify(documentRepository).save(documentArgumentCaptor.capture()); 
		assertThat(documentArgumentCaptor.getValue()).isEqualTo(document);

	}
	
	@Test
	void shouldNotAddSubmission() {
		// when
		when(conferenceRepository.findById(conference.getId())).thenReturn(Optional.empty());
		//Long actualResult = underTest.add(submission, conference.getId());
		
		// then
		//assertThat(actualResult).isEqualTo(-1);
	}

	@Test
	void souldFindAllSubmission() {
		// when
		when(submissionRepository.findAll()).thenReturn(Arrays.asList(submission));
		Optional<List<Submission>> submissionListOptional = underTest.findAllSubmission();
		List<Submission> submissionList = new ArrayList<>();
		submissionList.add(submission);

		// then
		assertThat(submissionListOptional).isNotEmpty();
		assertThat(submissionListOptional).contains(submissionList);
		verify(submissionRepository).findAll(); 
	}

	@Test
	void canCheckIfSubmissionExistsById() {
		// when
		when(submissionRepository.existsById(submission.getId())).thenReturn(true);
		boolean exists = underTest.existsById(submission.getId());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		
		// then
		assertThat(exists).isTrue(); 
		verify(submissionRepository).existsById(idArgumentCaptor.capture()); 
		assertThat(idArgumentCaptor.getValue()).isEqualTo(submission.getId()); 
	}
	
	@Test
	void canFindSubmissionExistsById() {
		// when
		when(submissionRepository.findById(submission.getId())).thenReturn(Optional.of(submission));
		Optional<Submission> submissionOptional = underTest.findById(submission.getId());
		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		
		// then
		assertThat(submissionOptional).isNotEmpty(); 
		assertThat(submissionOptional).hasValue(submission);
		verify(submissionRepository).findById(idArgumentCaptor.capture()); 
		assertThat(idArgumentCaptor.getValue()).isEqualTo(submission.getId()); 
	}

}
