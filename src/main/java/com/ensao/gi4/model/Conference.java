package com.ensao.gi4.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String acronym;
	private String venue;
	private String city;
	private String country;
	private LocalDate firstDay;
	private LocalDate lastDay;
	private String primaryArea;
	private String secondaryArea;
	private String organizer;
	private String phoneNumber;
	private String otherInfo;
	@OneToMany(mappedBy = "conference")
	@JsonManagedReference
	private List<Submission> submissions;
	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonManagedReference
	private User user;
	@OneToOne(mappedBy = "conference")
	@JsonBackReference
	private CallForPapers callForPapers;

	public Conference(String name, String acronym, String venue, String city, String country, LocalDate firstDay,
			LocalDate lastDay, String primaryArea, String secondaryArea, String organizer) {
		this.name = name;
		this.acronym = acronym;
		this.venue = venue;
		this.city = city;
		this.country = country;
		this.firstDay = firstDay;
		this.lastDay = lastDay;
		this.primaryArea = primaryArea;
		this.secondaryArea = secondaryArea;
		this.organizer = organizer;
	}

}
