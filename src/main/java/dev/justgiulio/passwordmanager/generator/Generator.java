package dev.justgiulio.passwordmanager.generator;

public class Generator {

	public void generate(int length) {
		if(length < 1) {
			throw new IllegalArgumentException("Length value must be greater than 0");
		}
	}

}
