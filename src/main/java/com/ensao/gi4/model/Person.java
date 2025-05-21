package com.ensao.gi4.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id; 
	protected String firstname;
	protected String lastname; 
	protected String email;
	
	
}
