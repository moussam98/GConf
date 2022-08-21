package com.ensao.gi4.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensao.gi4.model.Person;
import com.ensao.gi4.repository.PersonRepository;
import com.ensao.gi4.service.api.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

	private PersonRepository personRepository;

	@Autowired
	public PersonServiceImpl(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Override
	public int addPerson(Person person) {

		boolean existsByEmail = personRepository.existsByEmail(person.getEmail());
		if (!existsByEmail) {
			personRepository.save(person);
			return 1;
		}
		return 0;
	}

	@Override
	public Optional<Person> findByEmail(String email) {
		return personRepository.findByEmail(email);
	}

	@Override
	public Optional<List<Person>> findAll() {
		return Optional.of(personRepository.findAll());
	}

	@Override
	public int deleteByEmail(String email) {
		return personRepository.deleteByEmail(email); 
	}

}
