package com.ensao.gi4.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ensao.gi4.model.Person;
import com.ensao.gi4.repository.PersonRepository;
import com.ensao.gi4.service.api.PersonService;
import com.ensao.gi4.service.impl.PersonServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	@Mock
	private PersonRepository personRepository;
	private PersonService underTest;

	@BeforeEach
	void setUp() {
		underTest = new PersonServiceImpl(personRepository);
	}

	@Test
	void canAddPerson() {
		// given
		Person person = new Person(null, "Ali", "Moussa", "ali@mail.com", "Niger", "ENSA");

		// when
		int result = underTest.addPerson(person);
		ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);

		// then
		assertThat(result).isEqualTo(1);

		verify(personRepository).save(personArgumentCaptor.capture());

		assertThat(personArgumentCaptor.getValue()).isEqualTo(person);
	}

	@Test
	void shouldNotAddPersonIfEmailExist() {
		// given
		Person person = new Person(null, "Ali", "Moussa", "ali@gmail.com", "Niger", "ENSA");

		// when
		when(personRepository.existsByEmail(person.getEmail())).thenReturn(true);
		int result = underTest.addPerson(person);

		// then
		assertThat(result).isEqualTo(0);
	}

	@Test
	void shouldReturnPersonIfEmailExists() {
		// given
		String email = "ali@gmail.com";
		Person person = new Person(null, "Ali", "Moussa", email, "Niger", "ENSA");

		// when
		when(personRepository.findByEmail(email)).thenReturn(Optional.of(person));
		Optional<Person> personOptional = underTest.findByEmail(email);
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

		// then
		verify(personRepository).findByEmail(argumentCaptor.capture());
		assertThat(email).isEqualTo(argumentCaptor.getValue());
		assertThat(personOptional).isNotEmpty();
		assertThat(personOptional).hasValue(person);
	}
	
	@Test
	void shouldNotReturnPersonIfEmailDoesNotExists() {
		// given
		String email = "ali@gmail.com";
	
		// when
		when(personRepository.findByEmail(email)).thenReturn(Optional.empty());
		Optional<Person> personOptional = underTest.findByEmail(email);
		
		// then
		assertThat(personOptional).isEmpty();
	}
	
	@Test
	void shouldReturnAllPersonIfExist() {
		// given 
		Person person = new Person(null, "Ali", "Moussa", "ali@gmail.com", "Morocco", "ENSAO");
		Person person1 = new Person(null, "Abdou", "Moussa", "abdou@gmail.com", "Morocco", "ENSAO");
		List<Person> personList = Arrays.asList(person, person1); 
		
		// when 
		when(personRepository.findAll()).thenReturn(personList); 
		Optional<List<Person>> result = underTest.findAll(); 
	
		// then
		assertThat(result).isNotEmpty();
		assertThat(result).containsSame(personList); 
		verify(personRepository).findAll(); 
	}
	
	@Test
	void shouldNotReturnPerson() {
		// given 
		
		// when 
		Optional<List<Person>> result = underTest.findAll(); 
		
		// then
		verify(personRepository).findAll(); 
		assertThat(result.get()).isEmpty();
	}
	
	@Test
	void shouldDeletePersonIfEmailExist() {
		// given 
		String email = "ali@gmail.com"; 
		
		// when 
		when(personRepository.deleteByEmail(email)).thenReturn(1); 
		int result = underTest.deleteByEmail(email); 
		
		
		// then
		verify(personRepository).deleteByEmail(email); 
		assertThat(result).isEqualTo(1);
	}
	
	@Test
	void shouldNotDeletePersonIfEmailDoesNotExist() {
		// given 
		String email = "ali@gmail.com"; 
		
		// when 
		int result = underTest.deleteByEmail(email); 
		
		
		// then
		verify(personRepository).deleteByEmail(email); 
		assertThat(result).isEqualTo(0);
	}

}
