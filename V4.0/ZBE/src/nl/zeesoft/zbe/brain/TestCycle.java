package nl.zeesoft.zbe.brain;

public class TestCycle extends Cycle {
	public float[]		expectedOutputs		= null;
	
	public void initialize(Brain brain) {
		super.initialize(brain);
		expectedOutputs = new float[brain.getOutputLayer().neurons.size()];
		for (int i = 0; i < expectedOutputs.length; i++) {
			expectedOutputs[i] = 0.0F;
		}
	}
}
