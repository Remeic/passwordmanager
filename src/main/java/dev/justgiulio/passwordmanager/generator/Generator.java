package dev.justgiulio.passwordmanager.generator;

import java.security.SecureRandom;

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
		
		if(length < 1 || length > 32) {
			throw new IllegalArgumentException("Length value must be beetween than 1 and 32");
		}
		if(strength < 0 || strength > 2) {
			throw new IllegalArgumentException("Length value must be beetween than 0 and 2");
		}
		String choosedDictionary = this.getDictionary(strength);
		StringBuilder result = new StringBuilder(length);
		int randomCharPosition;
		for (int i = 0; i < length; i++) {
			randomCharPosition = randomizer.nextInt(choosedDictionary.length());
			result.append(choosedDictionary.charAt(randomCharPosition));
		}
		return result.toString();
	}



	public void setRandomizer(SecureRandom randomizer) {
		this.randomizer = randomizer;
	}
	
	
	/**
	 * Private methods
	 */
	
	private String getDictionary(int strength) {
		String resultDictionary;
		switch (strength) {
		case 0:
			resultDictionary = this.dictionaryLetters;
			break;
		case 1:
			resultDictionary = this.dictionaryLetters + this.dictionaryNumbers;
			break;
		case 2:
			resultDictionary = this.dictionaryLetters + this.dictionaryNumbers + this.dictionarySymbols;;
			break;
		default:
			resultDictionary = this.dictionaryLetters;
			break;
		}
		return resultDictionary;
	}


}
