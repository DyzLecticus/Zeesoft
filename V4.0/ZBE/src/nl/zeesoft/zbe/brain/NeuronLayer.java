package nl.zeesoft.zbe.brain;

import java.util.ArrayList;
import java.util.List;

public class NeuronLayer {
	public List<Neuron>		neurons		= new ArrayList<Neuron>();

	public void destroy() {
		for (Neuron neuron: neurons) {
			neuron.destroy();
		}
		neurons.clear();
	}
}
