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
	public void testGenerateWithLengthLowerThanOneThrowException() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			generator.generate(0);
		});
	}

}
