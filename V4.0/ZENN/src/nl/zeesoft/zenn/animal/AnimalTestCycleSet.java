package nl.zeesoft.zenn.animal;

import nl.zeesoft.zenn.network.NN;
import nl.zeesoft.zenn.network.TestCycle;
import nl.zeesoft.zenn.network.TestCycleSet;

public class AnimalTestCycleSet extends TestCycleSet {

	public void initialize(NN nn,boolean herbivore) {
		TestCycle tc = null;
		
		int leftFoodInput = AnimalNN.LEFT_GREEN;
		int frontFoodInput = AnimalNN.FRONT_GREEN;
		int rightFoodInput = AnimalNN.RIGHT_GREEN;
		if (!herbivore) {
			leftFoodInput = AnimalNN.LEFT_BLUE;
			frontFoodInput = AnimalNN.FRONT_BLUE;
			rightFoodInput = AnimalNN.RIGHT_BLUE;
		}
		
		tc = addTestCycle(nn);
		tc.inputs[AnimalNN.FRONT_BLACK] = 1.0F; 
		tc.expectedOutputs[AnimalNN.OUT_BACK] = 1.0F;

		// Move toward food
		for (int d = 3; d >= 1; d--) {
			tc = addTestCycle(nn);
			if (d>1) {
				tc.level = 1;
			}
			tc.inputs[frontFoodInput] = AnimalNN.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalNN.OUT_BACK] = 1.0F;
		}
		
		// Eat food
		tc = addTestCycle(nn);
		tc.inputs[frontFoodInput] = AnimalNN.INTENSITIES[0]; 
		tc.expectedOutputs[AnimalNN.OUT_FRONT] = 1.0F;

		// Turn toward food
		for (int d = 3; d >= 0; d--) {
			tc = addTestCycle(nn);
			if (d>1) {
				tc.level = 1;
			}
			tc.inputs[leftFoodInput] = AnimalNN.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalNN.OUT_RIGHT] = 1.0F;

			tc = addTestCycle(nn);
			if (d>1) {
				tc.level = 1;
			}
			tc.inputs[rightFoodInput] = AnimalNN.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalNN.OUT_LEFT] = 1.0F;
		}

		// Turn away from walls
		for (int d = 3; d >= 0; d--) {
			tc = addTestCycle(nn);
			if (d>1) {
				tc.level = 1;
			}
			tc.inputs[AnimalNN.FRONT_GREY] = AnimalNN.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalNN.OUT_RIGHT] = 1.0F;
			tc.expectedOutputs[AnimalNN.OUT_LEFT] = 1.0F;
		}

		// Turn away from corners
		for (int d = 3; d >= 0; d--) {
			tc = addTestCycle(nn);
			if (d>1) {
				tc.level = 1;
			}
			tc.inputs[AnimalNN.FRONT_GREY] = AnimalNN.INTENSITIES[d]; 
			tc.inputs[AnimalNN.LEFT_GREY] = AnimalNN.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalNN.OUT_LEFT] = 1.0F;
			
			tc = addTestCycle(nn);
			tc.inputs[AnimalNN.FRONT_GREY] = AnimalNN.INTENSITIES[d]; 
			tc.inputs[AnimalNN.RIGHT_GREY] = AnimalNN.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalNN.OUT_RIGHT] = 1.0F;
		}

		// Turn/move away from carnivores
		for (int d = 3; d >= 0; d--) {
			tc = addTestCycle(nn);
			tc.level = 1;
			tc.inputs[AnimalNN.LEFT_RED] = AnimalNN.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalNN.OUT_BACK] = 1.0F;

			tc = addTestCycle(nn);
			tc.level = 1;
			tc.inputs[AnimalNN.FRONT_RED] = AnimalNN.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalNN.OUT_RIGHT] = 1.0F;
			tc.expectedOutputs[AnimalNN.OUT_LEFT] = 1.0F;
			
			tc = addTestCycle(nn);
			tc.level = 1;
			tc.inputs[AnimalNN.RIGHT_RED] = AnimalNN.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalNN.OUT_BACK] = 1.0F;
		}
	}
	
	@Override
	protected TestCycleSet getCopyTestCycleSet() {
		return new AnimalTestCycleSet();
	}
}
