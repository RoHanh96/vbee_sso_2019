package com.vbee.springbootmongodbnewspapersrestapi.ulti;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

public class Slug {

	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	public static String makeSlug(String input) {
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		return slug.toLowerCase(Locale.ENGLISH);
	}

	public static String makeSlugByEmail(String email) {
		try {
			return email.split("@")[0];
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error when parse slug USER: " + e.getMessage());
		}
		return "";
	}
}