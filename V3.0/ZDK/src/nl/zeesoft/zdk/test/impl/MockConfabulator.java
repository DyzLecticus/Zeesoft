package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.confabulator.Confabulator;
import nl.zeesoft.zdk.test.MockObject;

public class MockConfabulator extends MockObject {
	private static final String[] TRAINING_SENTENCES = {
		"What are you? I am an artificial cognition.",
		"What is cognition? Cognition refers to mental processes within the brain.",
		"What is your name? My name is Dyz Lecticus.",
		"What is your goal? My goal is to model reality through interactions with people.",
		"Who created you? I was created by Andre van der Zee.",
		};	
	private static final String[] TRAINING_CONTEXTS = {
		"I Self Am Artificial Cognition",
		"Cognition",
		"I Self My Name",
		"I Self My Goal",
		"I Self My Creator",
		};

	@Override
	protected void describe() {
		System.out.println("This test uses the *MockConfabulator*.  ");
		System.out.println("The training set used to train this *MockConfabulator* consists of the following sequence and context combinations;  ");
		for (int i = 0; i < TRAINING_SENTENCES.length; i++) {
			System.out.println(" * *'" + TRAINING_SENTENCES[i] + "' - '" + TRAINING_CONTEXTS[i] + "'*");
		}
	}

	@Override
	protected Object initialzeMock() {
		Confabulator confabulator = new Confabulator();
		confabulator.setLog(true);
		for (int i = 0; i < TRAINING_SENTENCES.length; i++) {
			confabulator.learnSequence(TRAINING_SENTENCES[i],TRAINING_CONTEXTS[i]);
		}
		return confabulator;
	}
}
