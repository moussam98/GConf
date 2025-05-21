package com.ensao.gi4.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "Callforpapers")
public class CallForPapers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conference_id", referencedColumnName = "id")
	@JsonManagedReference
	private Conference conference; 
	private LocalDate startDate; 
	private LocalDate endDate; 
	@OneToMany
	Set<Topic> topics = new HashSet<>(); 
	private String guidelines; 
}
