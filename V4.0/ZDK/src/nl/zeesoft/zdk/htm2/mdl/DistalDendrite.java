package nl.zeesoft.zdk.htm2.mdl;

import java.util.ArrayList;
import java.util.List;

public class DistalDendrite extends Dendrite {
	public List<DistalSynapse>	synapses		= new ArrayList<DistalSynapse>();
	
	public DistalDendrite(String cellId) {
		super(cellId);
	}

	@Override
	public DistalDendrite copy() {
		DistalDendrite copy = new DistalDendrite(cellId);
		copy.setId(getId());
		for (DistalSynapse synapse: synapses) {
			copy.synapses.add(synapse.copy());
		}
		return copy;
	}
}
