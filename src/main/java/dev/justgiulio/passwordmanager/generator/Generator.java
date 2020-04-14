package dev.justgiulio.passwordmanager.generator;

import java.security.SecureRandom;

public class Generator {
	
	private String dictionary;
	private SecureRandom randomizer;
	
	public Generator() {
		super();
		this.dictionary = "abcdefghijklmnopqrstuvwxyz";
		setRandomizer(new SecureRandom());
	}
	
	

	public Generator(String dictionary) {
		super();
		this.dictionary = dictionary;
	}



	public String generate(int length, int strength) {
		if(length < 1 || length > 32) {
			throw new IllegalArgumentException("Length value must be beetween than 1 and 32");
		}
		if(strength < 0 || strength > 2) {
			throw new IllegalArgumentException("Length value must be beetween than 0 and 2");
		}
		StringBuilder result = new StringBuilder(length);
		int randomCharPosition;
		for (int i = 0; i < length; i++) {
			randomCharPosition = randomizer.nextInt(this.dictionary.length());
			result.append(dictionary.charAt(randomCharPosition));
		}
		return result.toString();
	}



	public void setRandomizer(SecureRandom randomizer) {
		this.randomizer = randomizer;
	}
	
	


}
