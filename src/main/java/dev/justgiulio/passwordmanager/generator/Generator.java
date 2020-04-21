package dev.justgiulio.passwordmanager.generator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Generator {

	private static final String DICTIONARY_LETTERS = "abcdefghijklmnopqrstuvwxyz";;
	private static final String DICTIONARY_NUMBERS = "0123456789";
	private static final String DICTIONARY_SYMBOLS = "!@#$%&*()_+-=[]?{};:_-<>";
	private static final int LOW_STRENGTH = 0;
	private static final int MEDIUM_STRENGTH = 1;
	private static final int HIGH_STRENGTH = 2;
	

	private SecureRandom randomizer;

	public Generator() {
		super();
		setRandomizer(new SecureRandom());
	}
 
	public String generate(int length, int strength) {
		if (length < 1 || length > 32) {
			throw new IllegalArgumentException("Length value must be beetween than 1 and 32");
		}
		if (strength < 0 || strength > 2) {
			throw new IllegalArgumentException("Length value must be beetween than 0 and 2");
		}
		String password;
		do {
			password = getPassword(length, this.getDictionary(strength));
		} while (!isValidPassword(password,strength));
		return password;

	}

	
	public void setRandomizer(SecureRandom randomizer) {
		this.randomizer = randomizer;
	}

	/**
	 * Private methods
	 */
	
	private boolean isValidPassword(String password, int strength) {
		boolean result = false;
		if(strength == LOW_STRENGTH) {
			result = true;
		}
		else if(strength == MEDIUM_STRENGTH) {
			result = isCharContained(password, DICTIONARY_LETTERS) && isCharContained(password,DICTIONARY_NUMBERS);
		}
		else if(strength == HIGH_STRENGTH) {
			result = isCharContained(password, DICTIONARY_LETTERS) && isCharContained(password,DICTIONARY_NUMBERS) && isCharContained(password,DICTIONARY_SYMBOLS);
		}
		return result;
	}
	
	private boolean isCharContained(String password, String dictionary) {
		boolean result  = false;
		for(char character:dictionary.toCharArray()) {
			if(password.indexOf(character) != -1) {
				result = true;
			}
		}
		return result;
	}

	
	private String getPassword(int length, String dictionary) {
		IntStream intStream =  randomizer.ints(length, 14, dictionary.length());
		StringBuilder result = new StringBuilder();
		for(int charPosition:intStream.toArray()) {
			result.append(Character.toString(dictionary.charAt(charPosition)));
		}
		return result.toString();
	}
	
	private String getDictionary(int strength) {
		String resultDictionary = DICTIONARY_LETTERS;
		if(strength == MEDIUM_STRENGTH) {
			resultDictionary = DICTIONARY_LETTERS + DICTIONARY_NUMBERS;
		}
		else if(strength == HIGH_STRENGTH) {
			resultDictionary = DICTIONARY_LETTERS + DICTIONARY_NUMBERS + DICTIONARY_SYMBOLS;

		}
		return resultDictionary;
	}


}
