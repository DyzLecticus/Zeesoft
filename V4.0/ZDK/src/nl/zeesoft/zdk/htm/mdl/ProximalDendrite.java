package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.List;

public class ProximalDendrite extends Dendrite {
	public List<ProximalSynapse>	synapses		= new ArrayList<ProximalSynapse>();
	
	public ProximalDendrite(String columnId) {
		super(columnId);
	}

	@Override
	public ProximalDendrite copy() {
		ProximalDendrite copy = new ProximalDendrite(cellId);
		copy.setId(getId());
		for (ProximalSynapse synapse: synapses) {
			copy.synapses.add(synapse.copy());
		}
		return copy;
	}
}
