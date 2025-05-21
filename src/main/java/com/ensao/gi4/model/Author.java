package com.ensao.gi4.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SubmissionAuthor")
public class Author extends Person {
	
	private String country;
	private String organization;

}
