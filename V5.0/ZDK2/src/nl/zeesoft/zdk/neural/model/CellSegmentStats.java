package nl.zeesoft.zdk.neural.model;

import java.util.List;

public class CellSegmentStats {
	public String 	type			= "";
	public int		segments		= 0;
	public int		synapses		= 0;
	public int		activeSynapses	= 0;
	
	public CellSegmentStats(String type) {
		this.type = type;
	}
	
	public void addStats(CellSegmentStats s) {
		this.segments += s.segments;
		this.synapses += s.synapses;
		this.activeSynapses += s.activeSynapses;
	}
	
	public void addSegments(List<Segment> list, float permanenceThreshold) {
		for (Segment segment: list)  {
			segments++;
			for (Synapse syn: segment.synapses) {
				if (syn.permanence>0) {
					synapses++;
					if (syn.permanence > permanenceThreshold) {
						activeSynapses++;
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		if (segments>0) {
			r.append("- ");
			r.append(type);
			r.append(" segments: ");
			r.append(segments);
			r.append("\n");
			r.append("- ");
			r.append(type);
			r.append(" synapses: ");
			r.append(synapses);
			r.append(" (active: ");
			r.append(activeSynapses);
			r.append(")");
		}
		return r.toString();
	}
}
