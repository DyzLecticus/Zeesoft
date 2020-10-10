package nl.zeesoft.zdk.neural.model;

import nl.zeesoft.zdk.grid.Position;

public class Synapse {
	public Position	connectTo		= new Position();
	public float	permanence		= 0F;
	
	public Synapse() {
		
	}
	
	public Synapse(Synapse synapse) {
		copyFrom(synapse);
	}
	
	public void copyFrom(Synapse synapse) {
		connectTo = synapse.connectTo;
		permanence = synapse.permanence;
	}
}
