package nl.zeesoft.zdk.neural.model;

import nl.zeesoft.zdk.Str;

public class ModelStatistics {
	public int cells					= 0;
	
	public int proximalSegments			= 0;
	public int proximalSynapses			= 0;
	public int activeProximalSynapses	= 0;
	
	public int distalSegments			= 0;
	public int distalSynapses			= 0;
	public int activeDistalSynapses		= 0;
	
	public int apicalSegments			= 0;
	public int apicalSynapses			= 0;
	public int activeApicalSynapses		= 0;
	
	public Str getDebugLogStr() {
		Str r = new Str();
		r.sb().append("- Cells             : ");
		r.sb().append(cells);
		if (proximalSegments>0) {
			r.sb().append("\n");
			r.sb().append("- Proximal segments : ");
			r.sb().append(proximalSegments);
			r.sb().append("\n");
			r.sb().append("- Proximal synapses : ");
			r.sb().append(proximalSynapses);		
			r.sb().append(" (active: ");
			r.sb().append(activeProximalSynapses);
			r.sb().append(")");
		}
		if (distalSegments>0) {
			r.sb().append("\n");
			r.sb().append("- Distal segments   : ");
			r.sb().append(distalSegments);
			r.sb().append("\n");
			r.sb().append("- Distal synapses   : ");
			r.sb().append(distalSynapses);		
			r.sb().append(" (active: ");
			r.sb().append(activeDistalSynapses);
			r.sb().append(")");
		}
		if (apicalSegments>0) {
			r.sb().append("\n");
			r.sb().append("- Apical segments   : ");
			r.sb().append(apicalSegments);
			r.sb().append("\n");
			r.sb().append("- Apical synapses   : ");
			r.sb().append(apicalSynapses);		
			r.sb().append(" (active: ");
			r.sb().append(activeApicalSynapses);
			r.sb().append(")");
		}
		return r;
	}
	
	public void add(ModelStatistics s) {
		this.cells += s.cells;
		this.proximalSegments += s.proximalSegments;
		this.proximalSynapses += s.proximalSynapses;
		this.activeProximalSynapses += s.activeProximalSynapses;
		this.distalSegments += s.distalSegments;
		this.distalSynapses += s.distalSynapses;
		this.activeDistalSynapses += s.activeDistalSynapses;
		this.apicalSegments += s.apicalSegments;
		this.apicalSynapses += s.apicalSynapses;
		this.activeApicalSynapses += s.activeApicalSynapses;
	}
}
