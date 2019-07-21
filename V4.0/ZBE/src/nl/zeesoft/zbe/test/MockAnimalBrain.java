package nl.zeesoft.zbe.test;

import nl.zeesoft.zbe.animal.AnimalBrain;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.MockObject;

public class MockAnimalBrain extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockAnimalBrain*");
	}

	@Override
	protected Object initialzeMock() {
		long started = System.currentTimeMillis();
		System.out.println("Generating trainable animal brain ...");
		AnimalBrain brain = AnimalBrain.getTrainableAnimalBrain(true,1,1000);
		if (brain==null) {
			brain = new AnimalBrain();
			ZStringBuilder err = brain.initialize();
			if (err.length()>0) {
				System.err.println(err);
			} else {
				System.err.println("Failed to generate trainable animal brain within one second");
			}
		} else {
			System.out.println("Generating trainable animal brain took " + (System.currentTimeMillis() - started) + " ms");
		}
		return brain;
	}

}
