package com.ensao.gi4.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@Table(name = "AppUser")
public class User extends Person {

	private String password;
	@Enumerated(EnumType.STRING)
	private Role role; 
	@OneToOne(mappedBy = "user")
	@JsonBackReference
	private Conference conference;
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());

		return Collections.singletonList(authority);
	}
}
