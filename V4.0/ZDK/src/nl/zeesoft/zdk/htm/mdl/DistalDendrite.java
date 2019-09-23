package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.List;

public class DistalDendrite extends Segment {
	public List<DistalSynapse>	synapses		= new ArrayList<DistalSynapse>();
	
	public DistalDendrite(String cellId, int index) {
		super(cellId,index);
	}
}
