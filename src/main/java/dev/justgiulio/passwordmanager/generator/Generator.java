package dev.justgiulio.passwordmanager.generator;

import java.security.SecureRandom;
import java.util.stream.Collectors;

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
		return randomizer.ints(length, 0, this.getDictionary(strength).length())
				.mapToObj(letterNumber -> this.fromCharToString(this.getDictionary(strength), letterNumber)).collect(Collectors.joining());

	}

	public void setRandomizer(SecureRandom randomizer) {
		this.randomizer = randomizer;
	}

	/**
	 * Private methods
	 */

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

	private String fromCharToString(String dictionary, int position) {
		return Character.toString(dictionary.charAt(position));
	}

}
