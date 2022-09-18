package com.ensao.gi4.utils;

import java.util.function.Predicate;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Predicate<String>{

	@Override
	public boolean test(String email) {
		// TODO make validation email
		return true;
	}

}
