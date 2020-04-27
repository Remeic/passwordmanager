package dev.justgiulio.passwordmanager.generator;

public interface Generator {

	String generate(int length, int strength);

}