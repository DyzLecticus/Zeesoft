package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.grid.Grid;

public class DistalSegment {
	public List<Synapse>	synapses		= new ArrayList<Synapse>();
	public int 				activeSynapses	= 0;
	
	public Synapse getSynapseTo(int posX, int posY, int posZ) {
		Synapse r = null;
		for (Synapse synapse: synapses) {
			if (synapse.connectToPosX == posX && synapse.connectToPosY == posY && synapse.connectToPosZ == posZ) {
				r = synapse;
				break;
			}
		}
		return r;
	}
	
	public void calculateActiveSynapses(Grid activeCells) {
		
	}
}
