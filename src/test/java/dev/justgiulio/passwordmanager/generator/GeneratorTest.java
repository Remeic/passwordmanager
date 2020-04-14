package dev.justgiulio.passwordmanager.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.security.SecureRandom;
import java.util.stream.Stream;

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
		final int length = 14;
		final int strength = 0;
		final String dictionary = "abcdefghijklmnopqrstuvwxyz";
		String generatedPassword = generator.generate(length, strength);
		Stream.of(generatedPassword.toCharArray())
	      .forEach(character -> assertThat(dictionary.toCharArray()).contains(character));

	}
	
	
	
}
