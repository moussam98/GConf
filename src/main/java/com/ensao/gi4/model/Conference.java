package com.ensao.gi4.model;


import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Conference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String acronym; 
	private String venue; 
	private String city; 
	private String country; 
	private Date firstDay; 
	private Date lastDay; 
	private String primaryArea; 
	private String secondaryArea; 
	private String organizer; 
	private String phoneNumber; 
	private String otherInfo; 
	@OneToMany(mappedBy = "conference")
	private List<Submission> submission;
	@OneToOne
	@JoinColumn(name = "person_id")
	private Person person; 
	@OneToOne(mappedBy = "conference")
	private CallForPapers callForPapers;
	
}
