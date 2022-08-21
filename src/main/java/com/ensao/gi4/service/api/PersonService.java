package com.ensao.gi4.service.api;

import java.util.List;
import java.util.Optional;

import com.ensao.gi4.model.Person;

public interface PersonService {
	int addPerson(Person person); 
	Optional<Person> findByEmail(String email); 
	Optional<List<Person>> findAll(); 
	int deleteByEmail(String email);
}
