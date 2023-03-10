package com.ensao.gi4.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

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
