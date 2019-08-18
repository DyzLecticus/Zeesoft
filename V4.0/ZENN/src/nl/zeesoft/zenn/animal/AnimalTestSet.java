package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.neural.Test;
import nl.zeesoft.zdk.neural.TestSet;

public class AnimalTestSet {
	public static TestSet getNewAnimalTestSet(boolean herbivore) {
		TestSet r = new TestSet(AnimalConstants.INPUTS.length,AnimalConstants.OUTPUTS.length);
		
		Test t = null;
		
		// Move forward by default
		t = r.addNewTest();
		t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
		
		addMoveBetweenWallsTests(r);
		addFoodTests(r,herbivore);
		addFrontalAvoidTests(r,herbivore);
		addWallCornerAvoidTests(r,herbivore);
		
		return r;
	}

	public static void addMoveBetweenWallsTests(TestSet ts) {
		Test t = null;
		for (float rand = 0; rand <=1; rand++) {
			for (int d1 = 3; d1 >= 0; d1--) {
				for (int d2 = 3; d2 >= 0; d2--) {
					t = ts.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,0,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d1]);
					setInputColor(t.inputs,2,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d2]);
					t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
				}
			}
		}
	}

	public static void addFoodTests(TestSet ts,boolean herbivore) {
		Test t = null;
		
		float[] foodColor = AnimalConstants.getFoodColor(herbivore);
		
		for (float rand = 0; rand <=1; rand++) {
			for (int d1 = 3; d1 >= 0; d1--) {
				t = ts.addNewTest();
				t.inputs[AnimalConstants.IN_RANDOM] = rand;
				setInputColor(t.inputs,0,foodColor,AnimalConstants.INTENSITIES[d1]);
				t.expectations[AnimalConstants.OUT_RIGHT_ACTUATOR] = 1;
				
				t = ts.addNewTest();
				t.inputs[AnimalConstants.IN_RANDOM] = rand;
				setInputColor(t.inputs,1,foodColor,AnimalConstants.INTENSITIES[d1]);
				if (d1>0) {
					t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
				} else {
					t.expectations[AnimalConstants.OUT_FRONT_MOUTH] = 1;
				}

				t = ts.addNewTest();
				t.inputs[AnimalConstants.IN_RANDOM] = rand;
				setInputColor(t.inputs,2,foodColor,AnimalConstants.INTENSITIES[d1]);
				t.expectations[AnimalConstants.OUT_LEFT_ACTUATOR] = 1;

				for (int d2 = 3; d2 >= 0; d2--) {
					t = ts.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,1,foodColor,AnimalConstants.INTENSITIES[d1]);
					setInputColor(t.inputs,2,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d2]);
					if (d1>0) {
						t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
					} else {
						t.expectations[AnimalConstants.OUT_FRONT_MOUTH] = 1;
					}

					t = ts.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,2,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d2]);
					setInputColor(t.inputs,1,foodColor,AnimalConstants.INTENSITIES[d1]);
					if (d1>0) {
						t.expectations[AnimalConstants.OUT_BACK_ACTUATOR] = 1;
					} else {
						t.expectations[AnimalConstants.OUT_FRONT_MOUTH] = 1;
					}
				}
			}
		}
	}
	
	public static void addFrontalAvoidTests(TestSet ts,boolean herbivore) {
		Test t = null;
		
		float[][] avoidColors = AnimalConstants.getAvoidColors(herbivore);
		for (float rand = 0; rand <=1; rand++) {
			for (int c = 0; c < avoidColors.length; c++) {
				for (int d1 = 3; d1 >= 0; d1--) {
					t = ts.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,1,avoidColors[c],AnimalConstants.INTENSITIES[d1]);
					if (rand==0) {
						t.expectations[AnimalConstants.OUT_LEFT_ACTUATOR] = 1;
					} else {
						t.expectations[AnimalConstants.OUT_RIGHT_ACTUATOR] = 1;
					}
				}
			}
		}
	}
	
	public static void addWallCornerAvoidTests(TestSet ts,boolean herbivore) {
		Test t = null;
		
		for (float rand = 0; rand <=1; rand++) {
			for (int d1 = 1; d1 >= 0; d1--) {
				if (d1==0 || rand==0) {
					t = ts.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,0,AnimalConstants.COLOR_GREY,1);
					setInputColor(t.inputs,1,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d1]);
					t.expectations[AnimalConstants.OUT_LEFT_ACTUATOR] = 1;

					t = ts.addNewTest();
					t.inputs[AnimalConstants.IN_RANDOM] = rand;
					setInputColor(t.inputs,1,AnimalConstants.COLOR_GREY,AnimalConstants.INTENSITIES[d1]);
					setInputColor(t.inputs,2,AnimalConstants.COLOR_GREY,1);
					t.expectations[AnimalConstants.OUT_RIGHT_ACTUATOR] = 1;
				}
			}
		}
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
