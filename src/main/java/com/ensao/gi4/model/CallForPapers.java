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

import lombok.Data;

@Entity
@Data
public class CallForPapers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	@OneToOne
	@JoinColumn(name = "conference_id", nullable = false)
	private Conference conference; 
	private Date startDate; 
	private Date endDate; 
	@OneToMany(mappedBy = "callForPapers")
	private List<Topic> topicList; 
	private String guidelines; 
}
