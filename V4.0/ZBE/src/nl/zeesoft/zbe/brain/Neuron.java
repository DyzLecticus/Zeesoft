package nl.zeesoft.zbe.brain;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
	public int					id			= -1;
	
	public List<NeuronLink>		targets		= new ArrayList<NeuronLink>();
	public List<NeuronLink>		sources		= new ArrayList<NeuronLink>();
	
	public float				threshold	= 0.5F;
	public float				value		= 0.0F;
	
	public Neuron(int id) {
		this.id = id;
	}
	
	public void destroy() {
		for (NeuronLink link: sources) {
			link.source = null;
			link.target = null;
		}
		targets.clear();
		sources.clear();
	}
}
