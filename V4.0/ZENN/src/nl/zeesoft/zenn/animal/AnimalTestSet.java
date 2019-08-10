package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.neural.Test;
import nl.zeesoft.zdk.neural.TestSet;

public class AnimalTestSet {
	public static TestSet getNewAnimalTestSet(boolean herbivore) {
		TestSet r = new TestSet(AnimalConstants.INPUTS.length,AnimalConstants.OUTPUTS.length);
		
		float[] foodColor = AnimalConstants.COLOR_GREEN;
		if (!herbivore) {
			foodColor = AnimalConstants.COLOR_BLUE;
		}
		
		Test t = null;
		
		// Move forward by default
		t = r.addNewTest();
		t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;

		for (float rand = 0; rand <=1; rand++) {
			for (int d = 3; d >= 0; d--) {
				// Food
				t = r.addNewTest();
				t.inputs[AnimalConstants.IN_RANDOM] = rand;
				setInputColor(t.inputs,0,foodColor,AnimalConstants.INTENSITIES[d]);
				t.expectations[AnimalConstants.OUT_RIGHT_ACTUATOR] = 1;

				t = r.addNewTest();
				t.inputs[AnimalConstants.IN_RANDOM] = rand;
				setInputColor(t.inputs,1,foodColor,AnimalConstants.INTENSITIES[d]);
				if (d>0) {
					t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
				} else {
					t.expectations[AnimalConstants.OUT_FRONT_MOUTH] = 1;
				}
				
				t = r.addNewTest();
				t.inputs[AnimalConstants.IN_RANDOM] = rand;
				setInputColor(t.inputs,2,foodColor,AnimalConstants.INTENSITIES[d]);
				t.expectations[AnimalConstants.OUT_LEFT_ACTUATOR] = 1;

				// Walls
				if (d>1) {
					t = r.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,0,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d]);
					t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
					
					t = r.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,2,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d]);
					t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
				} else if (d==0 || d==1 && rand==1) {
					t = r.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,0,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d]);
					setInputColor(t.inputs,1,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d]);
					t.expectations[AnimalConstants.OUT_LEFT_ACTUATOR] = 1;
	
					t = r.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,1,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d]);
					if (rand==0) {
						t.expectations[AnimalConstants.OUT_LEFT_ACTUATOR] = 1;
					} else {
						t.expectations[AnimalConstants.OUT_RIGHT_ACTUATOR] = 1;
					}
	
					t = r.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,1,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d]);
					setInputColor(t.inputs,2,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d]);
					t.expectations[AnimalConstants.OUT_RIGHT_ACTUATOR] = 1;
				}
				
				// Danger
				t = r.addNewTest();
				t.inputs[AnimalConstants.IN_RANDOM] = rand;
				setInputColor(t.inputs,0,AnimalConstants.COLOR_RED,AnimalConstants.INTENSITIES[d]);
				t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
				
				t = r.addNewTest();
				t.inputs[AnimalConstants.IN_RANDOM] = rand;
				setInputColor(t.inputs,1,AnimalConstants.COLOR_RED,AnimalConstants.INTENSITIES[d]);
				if (rand==0) {
					t.expectations[AnimalConstants.OUT_LEFT_ACTUATOR] = 1;
				} else {
					t.expectations[AnimalConstants.OUT_RIGHT_ACTUATOR] = 1;
				}
				
				t = r.addNewTest();
				t.inputs[AnimalConstants.IN_RANDOM] = rand;
				setInputColor(t.inputs,2,AnimalConstants.COLOR_RED,AnimalConstants.INTENSITIES[d]);
				t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
			}
		}

		return r;
	}

	public static void setInputColor(float[] inputs, int input, float[] color, float intensity) {
		if (input==0) {
			inputs[AnimalConstants.IN_LEFT_RED] = color[0] * intensity;
			inputs[AnimalConstants.IN_LEFT_GREEN] = color[1] * intensity;
			inputs[AnimalConstants.IN_LEFT_BLUE] = color[2] * intensity;
		} else if (input==1) {
			inputs[AnimalConstants.IN_FRONT_RED] = color[0] * intensity;
			inputs[AnimalConstants.IN_FRONT_GREEN] = color[1] * intensity;
			inputs[AnimalConstants.IN_FRONT_BLUE] = color[2] * intensity;
		} else if (input==2) {
			inputs[AnimalConstants.IN_RIGHT_RED] = color[0] * intensity;
			inputs[AnimalConstants.IN_RIGHT_GREEN] = color[1] * intensity;
			inputs[AnimalConstants.IN_RIGHT_BLUE] = color[2] * intensity;
		}
	}
}
