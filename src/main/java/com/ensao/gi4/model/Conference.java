package com.ensao.gi4.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conferences")
public class Conference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String acronym;
	private String venue;
	private String city;
	private String country;
	private LocalDate startDate;
	private LocalDate endDate;
	private String primaryArea;
	private String secondaryArea;
	private String organizer;
	private String phoneNumber;
	private String otherInfo;
	protected Instant createdAt;
	protected Instant updatedAt;
	@OneToMany(mappedBy = "conference", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Submission> submissions;
	@OneToOne
	@JoinColumn(name = "owner_Id")
	@JsonManagedReference
	private User owner;
	@OneToOne(mappedBy = "conference")
	@JsonBackReference
	private CallForPapers callForPapers;

	public Conference(String name, String acronym, String venue, String city, String country, LocalDate startDate,
					  LocalDate endDate, String primaryArea, String secondaryArea, String organizer) {
		this.name = name;
		this.acronym = acronym;
		this.venue = venue;
		this.city = city;
		this.country = country;
		this.startDate = startDate;
		this.endDate = endDate;
		this.primaryArea = primaryArea;
		this.secondaryArea = secondaryArea;
		this.organizer = organizer;
	}

}
