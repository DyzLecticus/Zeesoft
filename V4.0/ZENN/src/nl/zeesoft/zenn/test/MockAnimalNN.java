package nl.zeesoft.zenn.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zenn.animal.AnimalNN;

public class MockAnimalNN extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockAnimalNN*");
	}

	@Override
	protected Object initialzeMock() {
		long started = System.currentTimeMillis();
		System.out.println("Generating trainable animal neural network ...");
		AnimalNN nn = AnimalNN.getTrainableAnimalNN(true,1,1000);
		if (nn==null) {
			nn = new AnimalNN();
			ZStringBuilder err = nn.initialize();
			if (err.length()>0) {
				System.err.println(err);
			} else {
				System.err.println("Failed to generate trainable animal neural network within one second");
			}
		} else {
			System.out.println("Generating trainable animal neural network took " + (System.currentTimeMillis() - started) + " ms");
		}
		return nn;
	}

}
