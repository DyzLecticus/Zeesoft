package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
	public int					id			= -1;
	public int					posX		= -1;
	
	public List<NeuronLink>		targets		= new ArrayList<NeuronLink>();
	public List<NeuronLink>		sources		= new ArrayList<NeuronLink>();
	
	public float				threshold	= 0.5F;
	public float				value		= 0.0F;
	
	public Neuron(int id,int posX) {
		this.id = id;
		this.posX = posX;
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
