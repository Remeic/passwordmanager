package dev.justgiulio.passwordmanager.generator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SecureRandomGenerator implements Generator {

	private static final String DICTIONARY_LETTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final String DICTIONARY_NUMBERS = "0123456789";
	private static final String DICTIONARY_SYMBOLS = "!@#$%&*()_+-=[]?{};:_-<>";
	private static final int MEDIUM_STRENGTH = 1;
	private static final int HIGH_STRENGTH = 2;
	private static final int LOW_STRENGTH = 0;
	

	private SecureRandom randomizer;

	public SecureRandomGenerator() {
		super();
		setRandomizer(new SecureRandom());
	}
 
	@Override
	public String generate(int length, int strength) {
		if (length < 1 || length > 32) {
			throw new IllegalArgumentException("Length value must be beetween than 1 and 32");
		}
		if (strength < 0 || strength > 2) {
			throw new IllegalArgumentException("Length value must be beetween than 0 and 2");
		}
		return getPassword(length,strength);

	}

	
	public void setRandomizer(SecureRandom randomizer) {
		this.randomizer = randomizer;
	}

	/**
	 * Private methods
	 */
	

	private String getPassword(int length, int strength) {
		int nOfLetters = length/(strength+1);
		int nOfNumbers;
		int nOfSymbols;
		StringBuilder result = new StringBuilder(getCharsFromDictionary(nOfLetters,getDictionary(LOW_STRENGTH)));
		if(strength == MEDIUM_STRENGTH) {
			nOfNumbers = (length - nOfLetters);
			result.append(getCharsFromDictionary(nOfNumbers,getDictionary(MEDIUM_STRENGTH)));
		}
		else if (strength == HIGH_STRENGTH) {
			nOfNumbers = (length - nOfLetters)/2;
			result.append(getCharsFromDictionary(nOfNumbers,getDictionary(MEDIUM_STRENGTH)));
			nOfSymbols = (length - (nOfLetters + nOfNumbers));
			result.append(getCharsFromDictionary(nOfSymbols,getDictionary(HIGH_STRENGTH)));
		}
		return shufflePassword(result.toString());
	}
	
	private String shufflePassword(String password) {
		List<String> splittedPassword = new ArrayList<String>();
		for( char c: password.toCharArray()) {
			splittedPassword.add(Character.toString(c));
		}
		Collections.shuffle(splittedPassword);
		return splittedPassword.stream().map(n -> String.valueOf(n))
				.collect(Collectors.joining());
	}
	
	private String getCharsFromDictionary(int numbersOfChar, String dictionary) {
		IntStream intStream =  randomizer.ints(numbersOfChar, 0, dictionary.length());
		StringBuilder result = new StringBuilder();
		for(int charPosition:intStream.toArray()) {
			result.append(Character.toString(dictionary.charAt(charPosition)));
		}
		return result.toString();
	}
	
	
	
	
	private String getDictionary(int strength) {
		String resultDictionary = DICTIONARY_LETTERS;
		if(strength == MEDIUM_STRENGTH) {
			resultDictionary = DICTIONARY_NUMBERS;
		}
		else if(strength == HIGH_STRENGTH) {
			resultDictionary = DICTIONARY_SYMBOLS;

		}
		return resultDictionary;
	}


}
