package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.List;

public class ProximalDendrite extends Segment {
	public List<ProximalSynapse>	synapses		= new ArrayList<ProximalSynapse>();
	
	public ProximalDendrite(String columnId, int index) {
		super(columnId,index);
	}
}
