package nl.zeesoft.zbe.brain;

import java.util.ArrayList;
import java.util.List;

public class Cycle {
	public float[]			inputs				= null;
	public float[]			outputs				= null;
	public boolean			normalizeOutput		= true;
	
	public List<Neuron>		firedNeurons		= new ArrayList<Neuron>();
	public List<NeuronLink>	firedLinks			= new ArrayList<NeuronLink>();
	
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
	
	protected void finalize(Brain brain) {
		for (int n = 0; n < outputs.length; n++) {
			if (n<brain.getOutputLayer().neurons.size()) {
				Neuron output = brain.getOutputLayer().neurons.get(n);
				if (output.value>=output.threshold) {
					if (normalizeOutput) {
						outputs[n] = 1.0F;
					} else {
						outputs[n] = output.value;
					}
				}
			}
		}
	}
}
