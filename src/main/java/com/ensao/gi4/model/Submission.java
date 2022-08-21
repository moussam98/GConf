package com.ensao.gi4.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Submission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title; 
	private String description; 
	@OneToMany(mappedBy = "submission")
	private List<Keyword> keywords; 
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] file; 
	@ManyToOne
	@JoinColumn(name = "conference_id", nullable = false)
	private Conference conference;
	@OneToMany(mappedBy = "submission")
	private List<Person> authors; 

}
