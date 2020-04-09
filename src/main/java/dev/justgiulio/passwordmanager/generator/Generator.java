package dev.justgiulio.passwordmanager.generator;

public class Generator {

	public void generate(int length) {
		if(length < 1 || length > 32) {
			throw new IllegalArgumentException("Length value must be beetween than 1 and 32");
		}
	}

}
