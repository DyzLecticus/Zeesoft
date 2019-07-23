package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;

public class TrainingPath {
	public Neuron 			inputNeuron		= null;
	public List<Neuron> 	middleNeurons	= new ArrayList<Neuron>();
	public Neuron			outputNeuron	= null;
	public float			error			= 0.0F;
}
