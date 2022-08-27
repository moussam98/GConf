package com.ensao.gi4.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class CallForPapers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	@OneToOne
	@JoinColumn(name = "conference_id", referencedColumnName = "id")
	private Conference conference; 
	private LocalDate startDate; 
	private LocalDate endDate; 
	private String topic1;
	private String topic2;
	private String topic3;
	private String topic4;
	private String guidelines; 
}
