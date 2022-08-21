package com.ensao.gi4.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ensao.gi4.model.Person;

@DataJpaTest
public class PersonRepositoryTest {
	
	@Autowired
	private PersonRepository underTest; 
	
	@Test
	void canAddPerson() {
		// given
		Person person = new Person(null, "Ali", "Moussa", "ali@gmail.com", "Morocco", "ENSAO"); 
		
		// when 
		Person expectedPerson = underTest.save(person); 
		
		// then 
		assertThat(expectedPerson).isEqualTo(person);
	}
	
	@Test
	void isShouldCheckIfPersonEmailExits() {
		// given
		String email = "ali@gmail.com";
		Person person = new Person(null, "Ali", "Moussa", email, "Morocco", "ENSAO");
		underTest.save(person);
		
		// when 
		boolean exits = underTest.existsByEmail(email); 
		
		// then 
		assertThat(exits).isTrue();
	}
	
	@Test
	void shouldReturnPersonIfEmailExits() {
		// given
		String email = "ali@gmail.com";
		Person person = new Person(null, "Ali", "Moussa", email, "Morocco", "ENSAO");
		underTest.save(person);
		
		// when 
		Optional<Person> expectedPerson = underTest.findByEmail(email); 
			
		// then 
		assertThat(expectedPerson).isNotEmpty();
		assertThat(expectedPerson).hasValue(person);
	}
	
	@Test
	void shouldReturnEmptyIfEmailDoesNotExits() {
		// given
		String email = "ali@gmail.com";
		
		// when 
		Optional<Person> expectedPerson = underTest.findByEmail(email); 
		
		// then 
		assertThat(expectedPerson).isEmpty(); 
	}
	
	@Test
	void shouldFindAllPersons() {
		//given 
		Person person = new Person(null, "Ali", "Moussa", "ali@gmail.com", "Morocco", "ENSAO");
		Person person1 = new Person(null, "Abdou", "Moussa", "abdou@gmail.com", "Morocco", "ENSAO");
		underTest.save(person);
		underTest.save(person1); 
		
		//when 
		List<Person> personList = underTest.findAll(); 
		
		//then 
		assertThat(personList).isNotEmpty(); 
		assertThat(personList).contains(person1, person); 
	}
	
	@Test
	void shouldNotFindAllPersons() {
		//given 
	
		//when 
		List<Person> personList = underTest.findAll(); 
		
		//then 
		assertThat(personList).isEmpty();
	}
	
	@Test
	void shouldDeletePersonByEmail() {
		// given
		String email = "ali@gmail.com";
		Person person = new Person(null, "Ali", "Moussa", email, "Morocco", "ENSAO");
		underTest.save(person);
		
		// when
		int result = underTest.deleteByEmail(email); 
		
		// then 
		assertThat(result).isEqualTo(1); 
	}

}
