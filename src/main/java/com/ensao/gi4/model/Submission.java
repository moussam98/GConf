package com.ensao.gi4.model;

import com.ensao.gi4.repository.StringSetConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "submissions")
public class Submission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title; 
	private String description;
	@Convert(converter = StringSetConverter.class)
	@Column(name = "keywords", nullable = false)
	private Set<String> keywords = new HashSet<>();
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Document document;
	@ManyToOne
	@JoinColumn(name = "conference_id", nullable = false)
	@JsonBackReference
	private Conference conference;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Author> authors; 
	private Boolean isEvaluate = false; 
	private Boolean isValidate = false;
	private LocalDate createdAt;

}
