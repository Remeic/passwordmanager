package dev.justgiulio.passwordmanager.generator;

public class Generator {
	
	
	public Generator() {
		super();
	}

	public String generate(int length, int strength) {
		if(length < 1 || length > 32) {
			throw new IllegalArgumentException("Length value must be beetween than 1 and 32");
		}
		if(strength < 0 || strength > 2) {
			throw new IllegalArgumentException("Length value must be beetween than 0 and 2");
		}
		StringBuilder result = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			result.append("g");
		}
		return result.toString();
	}

}
