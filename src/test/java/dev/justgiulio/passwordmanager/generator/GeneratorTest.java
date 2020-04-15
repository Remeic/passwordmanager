package dev.justgiulio.passwordmanager.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GeneratorTest {

	Generator generator;

	@Before
	public void setUp() throws Exception {
		generator =  new Generator();
	}

	
	/**
	 *  Boundary Test
	 */
	
	@Test
	public void testGenerateWithInvalidBoundaryLengthValuesThrowException() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			generator.generate(0,0);
		});
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			generator.generate(33,0);
		});
	}
	
	@Test
	public void testGenerateWithStrengthValueLowerThanZeroThrowException() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			generator.generate(1,-1);
		});
	}
	
	@Test
	public void testGenerateWithInvalidMaxBoundaryStrengthValueThrowException() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			generator.generate(1,3);
		});
	}
	
	/**
	 * Fixed Length Test
	 */
	
	@Test
	public void testGeneratePasswordWithFixedLength() {
		final int length = 14;
		final int strength = 0;
		String generatedPassword = generator.generate(length, strength);
		assertThat(generatedPassword).hasSize(length);
	}
	
	/**
	 * Test with dictionary
	 */
	
	@Test
	public void testGeneratePasswordWithDictionary() {
		final int length = 4;
		final int strength = 0;
		final String dictionary = "abcdefghijklmnopqrstuvwxyz";
		SecureRandom randomizer = Mockito.mock(SecureRandom.class);
		generator.setRandomizer(randomizer);
		when(randomizer.nextInt(dictionary.length())).thenReturn(1,3,4,10);
		String generatedPassword = generator.generate(length, strength);
		Stream.of(generatedPassword.toCharArray())
	      .forEach(character -> assertThat(dictionary.toCharArray()).contains(character));

	}
	
	@Test
	public void testGeneratePasswordWithMediumDictionary() {
		final int length = 4;
		final int strength = 1;
		final String letters = "abcdefghijklmnopqrstuvwxyz";
		final String numbers = "0123456789";
		final String dictionary = letters + numbers;
		SecureRandom randomizer = Mockito.mock(SecureRandom.class);
		generator.setRandomizer(randomizer);
		when(randomizer.nextInt(dictionary.length())).thenReturn(1,letters.length(),letters.length() + numbers.length()/2,10);
		String generatedPassword = generator.generate(length, strength);
		Stream.of(generatedPassword.toCharArray())
	      .forEach(character -> assertThat(dictionary.toCharArray()).contains(character));

	}
	
	@Test
	public void testGeneratePasswordWithHighDictionary() {
		final int length = 4;
		final int strength = 2;
		final String letters = "abcdefghijklmnopqrstuvwxyz";
		final String numbers = "0123456789";
		final String symbols = "!@#$%&*()_+-=[]?{};:_-<>";
		final String dictionary = letters + numbers + symbols;
		SecureRandom randomizer = Mockito.mock(SecureRandom.class);
		generator.setRandomizer(randomizer);
		when(randomizer.nextInt(dictionary.length()))
			.thenReturn(1,letters.length(),letters.length() + numbers.length()/2,letters.length() + numbers.length() + symbols.length()/2);
		String generatedPassword = generator.generate(length, strength);
		Stream.of(generatedPassword.toCharArray())
	      .forEach(character -> assertThat(dictionary.toCharArray()).contains(character));

	}
	
	
	
	
	
}
