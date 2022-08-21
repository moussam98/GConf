package com.ensao.gi4.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@RequiredArgsConstructor
@ToString
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NonNull
	private Long id; 
	@NonNull
	private String firstname;
	@NonNull
	private String lastname; 
	@NonNull
	private String email;
	@NonNull
	private String country;
	@NonNull
	private String organization;  
	@ManyToOne
	@JoinColumn(name = "submission_id", nullable = false)
	private Submission submission;
}
