package com.ensao.gi4.model;

import java.time.LocalDate;
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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Conference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NonNull
	private String name;
	@NonNull
	private String acronym;
	@NonNull
	private String venue;
	@NonNull
	private String city;
	@NonNull
	private String country;
	@NonNull
	private LocalDate firstDay;
	@NonNull
	private LocalDate lastDay;
	@NonNull
	private String primaryArea;
	@NonNull
	private String secondaryArea;
	@NonNull
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
