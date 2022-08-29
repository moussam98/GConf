package com.ensao.gi4.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	@ManyToMany
	@JoinTable(
			name = "Submission_KeywordSubmission",
			joinColumns = { @JoinColumn(name = "submission_id") },
			inverseJoinColumns = { @JoinColumn(name = "keywordSubmission_id") }
			)
	private Set<Keyword> keywords = new HashSet<>(); 
	@OneToOne
	private Document document;
	@ManyToOne
	@JoinColumn(name = "conference_id", nullable = false)
	private Conference conference;
	@OneToMany(mappedBy = "submission")
	private List<Person> authors; 

}
