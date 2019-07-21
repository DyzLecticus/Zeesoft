package nl.zeesoft.zbe.brain;

public class NeuronLink {
	public int		id		= -1;
	public Neuron	source	= null;
	public Neuron	target	= null;
	public float	weight	= 0.5F;
	
	public NeuronLink(int id) {
		this.id = id;
	}
}
