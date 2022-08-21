package com.ensao.gi4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ensao.gi4.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	Optional<Person> findByEmail(String email);
	boolean existsByEmail(String email);
	int deleteByEmail(String email); 

}
