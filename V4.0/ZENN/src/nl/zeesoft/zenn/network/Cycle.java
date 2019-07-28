package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;

public class Cycle {
	public float[]			inputs				= null;
	public float[]			outputs				= null;
	public boolean			roundOutput		= true;
	
	public List<Neuron>		firedNeurons		= new ArrayList<Neuron>();
	
	public void initialize(NN nn) {
		inputs = new float[nn.getInputLayer().neurons.size()];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = 0.0F;
		}
		outputs = new float[nn.getOutputLayer().neurons.size()];
		for (int i = 0; i < outputs.length; i++) {
			outputs[i] = 0.0F;
		}
	}
	
	public void copy(Cycle toCycle) {
		toCycle.inputs = new float[inputs.length];
		for (int i = 0; i < toCycle.inputs.length; i++) {
			toCycle.inputs[i] = inputs[i];
		}
		toCycle.outputs = new float[outputs.length];
		for (int i = 0; i < toCycle.outputs.length; i++) {
			toCycle.outputs[i] = outputs[i];
		}
		toCycle.roundOutput = roundOutput;
	}
	
	protected void prepare() {
		firedNeurons.clear();
	}
	
	protected void finalize(NN nn) {
		for (int n = 0; n < outputs.length; n++) {
			if (n<nn.getOutputLayer().neurons.size()) {
				Neuron output = nn.getOutputLayer().neurons.get(n);
				if (roundOutput) {
					if (output.value>=0.5F) {
						outputs[n] = 1.0F;
					} else {
						outputs[n] = 0.0F;
					}
				} else {
					outputs[n] = output.value;
				}
			}
		}
	}
}
