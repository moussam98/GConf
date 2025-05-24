package com.ensao.gi4.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@Table(name = "AppUser")
public class User extends Person implements UserDetails {

	private String password;
	@Enumerated(EnumType.STRING)
	// TODO: User can have multiple roles
	private Role role; 
	@OneToOne(mappedBy = "user")
	@JsonBackReference
	private Conference conference;
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
		return Collections.singletonList(authority);
	}

	@Override
	public String getUsername() {
		return email;
	}

}
