package dev.justgiulio.passwordmanager.generator;

import java.security.SecureRandom;
import java.util.stream.IntStream;

public class Generator {

	private static final String DICTIONARY_LETTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final String DICTIONARY_NUMBERS = "0123456789";
	private static final String DICTIONARY_SYMBOLS = "!@#$%&*()_+-=[]?{};:_-<>";
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
		return getPassword(length, this.getDictionary(strength));

	}

	
	public void setRandomizer(SecureRandom randomizer) {
		this.randomizer = randomizer;
	}

	/**
	 * Private methods
	 */
	
	private String getPassword(int length, String dictionary) {
		IntStream intStream =  randomizer.ints(length, 0, dictionary.length());
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
