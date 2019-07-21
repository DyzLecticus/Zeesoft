package nl.zeesoft.zbe.animal;

import nl.zeesoft.zbe.brain.Cycle;
import nl.zeesoft.zbe.brain.TestCycle;
import nl.zeesoft.zbe.brain.TestCycleSet;

public class AnimalTestCycleSet extends TestCycleSet {
	public AnimalTestCycleSet(AnimalBrain brain,boolean herbivore) {
		TestCycle tc = null;
		
		int leftFoodInput = AnimalBrain.LEFT_GREEN;
		int frontFoodInput = AnimalBrain.FRONT_GREEN;
		int rightFoodInput = AnimalBrain.RIGHT_GREEN;
		if (!herbivore) {
			leftFoodInput = AnimalBrain.LEFT_BLUE;
			frontFoodInput = AnimalBrain.FRONT_BLUE;
			rightFoodInput = AnimalBrain.RIGHT_BLUE;
		}
		
		tc = new TestCycle();
		tc.initialize(brain);
		tc.expectedOutputs[AnimalBrain.OUT_BACK] = 1.0F;
		cycles.add(tc);

		// Move toward food
		for (int d = 3; d >= 1; d--) {
			tc = new TestCycle();
			tc.initialize(brain);
			tc.inputs[frontFoodInput] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_BACK] = 1.0F;
			cycles.add(tc);
		}
		
		// Eat food
		tc = new TestCycle();
		tc.initialize(brain);
		tc.inputs[frontFoodInput] = AnimalBrain.INTENSITIES[0]; 
		tc.expectedOutputs[AnimalBrain.OUT_FRONT] = 1.0F;
		cycles.add(tc);

		// Turn toward food
		for (int d = 3; d >= 0; d--) {
			tc = new TestCycle();
			tc.initialize(brain);
			tc.inputs[leftFoodInput] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_RIGHT] = 1.0F;
			cycles.add(tc);
			
			tc = new TestCycle();
			tc.initialize(brain);
			tc.inputs[rightFoodInput] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_LEFT] = 1.0F;
			cycles.add(tc);
		}

		// Turn away from walls
		for (int d = 3; d >= 0; d--) {
			tc = new TestCycle();
			tc.initialize(brain);
			tc.inputs[AnimalBrain.FRONT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_RIGHT] = 1.0F;
			tc.expectedOutputs[AnimalBrain.OUT_LEFT] = 1.0F;
			cycles.add(tc);
		}

		// Turn away from corners
		for (int d = 3; d >= 0; d--) {
			tc = new TestCycle();
			tc.initialize(brain);
			tc.inputs[AnimalBrain.FRONT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.inputs[AnimalBrain.LEFT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_LEFT] = 1.0F;
			cycles.add(tc);
			
			tc = new TestCycle();
			tc.initialize(brain);
			tc.inputs[AnimalBrain.FRONT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.inputs[AnimalBrain.RIGHT_GREY] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_RIGHT] = 1.0F;
			cycles.add(tc);
		}

		// Turn/move away from carnivores
		for (int d = 3; d >= 0; d--) {
			tc = new TestCycle();
			tc.initialize(brain);
			tc.inputs[AnimalBrain.LEFT_RED] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_BACK] = 1.0F;
			cycles.add(tc);

			tc = new TestCycle();
			tc.initialize(brain);
			tc.inputs[AnimalBrain.FRONT_RED] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_RIGHT] = 1.0F;
			tc.expectedOutputs[AnimalBrain.OUT_LEFT] = 1.0F;
			cycles.add(tc);
			
			tc = new TestCycle();
			tc.initialize(brain);
			tc.inputs[AnimalBrain.RIGHT_RED] = AnimalBrain.INTENSITIES[d]; 
			tc.expectedOutputs[AnimalBrain.OUT_BACK] = 1.0F;
			cycles.add(tc);
		}
	}
	
	public void toSystemOut() {
		int c = 0;
		for (Cycle cycle: cycles) {
			if (cycle instanceof TestCycle) {
				c++;
				TestCycle tc = (TestCycle) cycle;
				System.out.println("Test cycle: " + c + ", success: " + tc.success + ", fired neurons: " + tc.firedNeurons.size());
				for (int n = 0; n < tc.outputs.length; n++) {
					System.out.println("  Output: " + n + ": " + tc.outputs[n] + ", expected: " + tc.expectedOutputs[n] + ", error: " + tc.errors[n]);
				}
			}
		}
		System.out.println("Test cycle set average error: " + averageError + ", successes: " + successes);
	}
}
