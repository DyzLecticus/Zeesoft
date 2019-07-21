package nl.zeesoft.zbe.brain;

public class Cycle {
	public float[]			inputs		= null;
	public float[]			outputs		= null;
	
	public void initialize(Brain brain) {
		inputs = new float[brain.getInputLayer().neurons.size()];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = 0.0F;
		}
		outputs = new float[brain.getOutputLayer().neurons.size()];
		for (int i = 0; i < outputs.length; i++) {
			outputs[i] = 0.0F;
		}
	}
}
