package dev.justgiulio.passwordmanager.generator;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class GeneratorTest {

	Generator generator;

	@Before
	public void setUp() throws Exception {
		generator =  new Generator();
	}

	@Test
	public void testGenerateWithInvalidBoundaryLengthValuesThrowException() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			generator.generate(0);
		});
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			generator.generate(33);
		});
	}
	
	@Test
	public void testGenerateWithStrengthValueLowerThanZeroThrowException() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			generator.generate(1,-1);
		});
	}
	
}
