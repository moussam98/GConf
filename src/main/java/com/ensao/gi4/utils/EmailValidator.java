package com.ensao.gi4.utils;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String>{

	private final static Pattern EMAIL_PATTERN = Pattern.compile(
			"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
			Pattern.CASE_INSENSITIVE);

	@Override
	public boolean test(String email) {
		if (email == null)
			return false;
		Matcher matcher = EMAIL_PATTERN.matcher(email);
		return matcher.matches();
	}
}
