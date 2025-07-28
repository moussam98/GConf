package com.ensao.gi4.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id; 
	protected String firstName;
	protected String lastName;
	protected String email;
	protected Instant createdAt;
	protected Instant updatedAt;

	
}
