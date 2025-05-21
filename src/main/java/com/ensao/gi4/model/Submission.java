package com.ensao.gi4.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title; 
	private String description; 
	@OneToMany
	private Set<Keyword> keywords = new HashSet<>(); 
	@OneToOne
	private Document document;
	@ManyToOne
	@JoinColumn(name = "conference_id", nullable = false)
	@JsonBackReference
	private Conference conference;
	@OneToMany
	private List<Author> authors; 
	private Boolean isEvaluate = false; 
	private Boolean isValidate = false; 

}
