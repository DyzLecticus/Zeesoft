package nl.zeesoft.zbe.animal;

import nl.zeesoft.zbe.brain.TestCycle;
import nl.zeesoft.zbe.brain.TestCycleSet;

public class AnimalTestCycleSet extends TestCycleSet {

	public void initialize(AnimalBrain brain,boolean herbivore) {
		TestCycle tc = null;
		
		int leftFoodInput = AnimalBrain.LEFT_GREEN;
		int frontFoodInput = AnimalBrain.FRONT_GREEN;
		int rightFoodInput = AnimalBrain.RIGHT_GREEN;
		if (!herbivore) {
			leftFoodInput = AnimalBrain.LEFT_BLUE;
			frontFoodInput = AnimalBrain.FRONT_BLUE;
			rightFoodInput = AnimalBrain.RIGHT_BLUE;
		}
		
		tc = addTestCycle(brain);
		tc.inputs[AnimalBrain.FRONT_BLACK] = 1.0F; 
		tc.expectedOutputs[AnimalBrain.OUT_BACK] = 1.0F;

		// Move toward food
		for (int d = 3; d >= 1; d--) {
			tc = addTestCycle(brain);
			tc.inputs[frontFoodInput] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_BACK] = 1.0F;
		}
		
		// Eat food
		tc = addTestCycle(brain);
		tc.inputs[frontFoodInput] = AnimalBrain.INTENSITIES[0]; 
		tc.expectedOutputs[AnimalBrain.OUT_FRONT] = 1.0F;

		// Turn toward food
		for (int d = 3; d >= 0; d--) {
			tc = addTestCycle(brain);
			tc.inputs[leftFoodInput] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_RIGHT] = 1.0F;

			tc = addTestCycle(brain);
			tc.inputs[rightFoodInput] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_LEFT] = 1.0F;
		}

		// Turn away from walls
		for (int d = 3; d >= 0; d--) {
			tc = addTestCycle(brain);
			tc.inputs[AnimalBrain.FRONT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_RIGHT] = 1.0F;
			tc.expectedOutputs[AnimalBrain.OUT_LEFT] = 1.0F;
		}

		// Turn away from corners
		for (int d = 3; d >= 0; d--) {
			tc = addTestCycle(brain);
			tc.inputs[AnimalBrain.FRONT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.inputs[AnimalBrain.LEFT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_LEFT] = 1.0F;
			
			tc = addTestCycle(brain);
			tc.inputs[AnimalBrain.FRONT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.inputs[AnimalBrain.RIGHT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_RIGHT] = 1.0F;
		}

		// Turn/move away from carnivores
		for (int d = 3; d >= 0; d--) {
			tc = addTestCycle(brain);
			tc.level = 1;
			tc.inputs[AnimalBrain.LEFT_RED] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_BACK] = 1.0F;

			tc = addTestCycle(brain);
			tc.level = 1;
			tc.inputs[AnimalBrain.FRONT_RED] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_RIGHT] = 1.0F;
			tc.expectedOutputs[AnimalBrain.OUT_LEFT] = 1.0F;
			
			tc = addTestCycle(brain);
			tc.level = 1;
			tc.inputs[AnimalBrain.RIGHT_RED] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_BACK] = 1.0F;
		}
	}
	
	public void toSystemOut() {
		int c = 0;
		for (TestCycle tc: cycles) {
			c++;
			System.out.println("Test cycle: " + c + ", fired neurons: " + tc.firedNeurons.size() + ", fired links: " + tc.firedLinks.size() + ", level: " + tc.level + ", success: " + tc.success);
			for (int n = 0; n < tc.outputs.length; n++) {
				if (tc.outputs[n]!=tc.expectedOutputs[n]) {
					System.out.println("  Output: " + n + ": " + tc.outputs[n] + ", expected: " + tc.expectedOutputs[n] + ", error: " + tc.errors[n]);
				}
			}
		}
		System.out.println("Test cycle set average error: " + averageError + ", successes: " + successes);
		if (cyclesPerLevel.size()>0) {
			for (Integer key: cyclesPerLevel.keySet()) {
				int successes = 0;
				if (successesPerLevel.get(key)!=null) {
					successes = successesPerLevel.get(key);
				}
				System.out.println(" Level: " + key + ", successes: " + successes + "/" + cyclesPerLevel.get(key));
			}
		}
	}
	
	@Override
	protected TestCycleSet getCopyTestCycleSet() {
		return new AnimalTestCycleSet();
	}
}
