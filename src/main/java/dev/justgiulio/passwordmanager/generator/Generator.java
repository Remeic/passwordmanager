package dev.justgiulio.passwordmanager.generator;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class Generator {

	private String dictionaryLetters;
	private String dictionaryNumbers;
	private String dictionarySymbols;
	private SecureRandom randomizer;

	public Generator() {
		super();
		this.dictionaryLetters = "abcdefghijklmnopqrstuvwxyz";
		this.dictionaryNumbers = "0123456789";
		this.dictionarySymbols = "!@#$%&*()_+-=[]?{};:_-<>";
		setRandomizer(new SecureRandom());
	}

	public String generate(int length, int strength) {
		if (length < 1 || length > 32) {
			throw new IllegalArgumentException("Length value must be beetween than 1 and 32");
		}
		if (strength < 0 || strength > 2) {
			throw new IllegalArgumentException("Length value must be beetween than 0 and 2");
		}
		String choosedDictionary = this.getDictionary(strength);
		
		return randomizer.ints(length, 0, choosedDictionary.length())
				.mapToObj(x -> this.fromCharToString(choosedDictionary,x)).collect(Collectors.joining()); 
		
	}
	
	public void setRandomizer(SecureRandom randomizer) {
		this.randomizer = randomizer;
	}

	/**
	 * Private methods
	 */

	private String getDictionary(int strength) {
		String resultDictionary = this.dictionaryLetters;
		switch (strength) {
		case 1:
			resultDictionary = this.dictionaryLetters + this.dictionaryNumbers;
			break;
		case 2:
			resultDictionary = this.dictionaryLetters + this.dictionaryNumbers + this.dictionarySymbols;
			;
			break;
		}
		return resultDictionary;
	}
	
	private String fromCharToString(String dictionary, int position) {
		return Character.toString(dictionary.charAt(position));
	}

}
