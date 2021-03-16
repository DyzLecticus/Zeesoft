package nl.zeesoft.zdk.neural.model;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.neural.processor.sp.SpatialPooler;
import nl.zeesoft.zdk.neural.processor.tm.TemporalMemory;

public class CellStats {
	public int	cells					= 0;
	
	public int	proximalSegments		= 0;
	public int	proximalSynapses		= 0;
	public int	activeProximalSynapses	= 0;
	
	public int	distalSegments			= 0;
	public int	distalSynapses			= 0;
	public int	activeDistalSynapses	= 0;
	
	public int	apicalSegments			= 0;
	public int	apicalSynapses			= 0;
	public int	activeApicalSynapses	= 0;
	
	public CellStats() {
		
	}
	
	public CellStats(SpatialPooler sp) {
		addModelCells(this,sp.connections.toCells(this));
	}
	
	public CellStats(TemporalMemory tm) {
		addModelCells(this,tm.cells);
	}
	
	public void addStats(CellStats s) {
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
	
	public void addModelCells(Object caller, Cells modelCells) {
		Function function = new Function() {
			@Override
			protected Object exec() {
				addCell((Cell) param2, modelCells.config.permanenceThreshold);
				return param2;
			}
		};
		modelCells.applyFunction(caller, function);
	}
	
	public void addCell(Cell cell, float permanenceThreshold) {
		cells++;
		for (Segment seg: cell.proximalSegments)  {
			proximalSegments++;
			for (Synapse syn: seg.synapses) {
				if (syn.permanence>0) {
					proximalSynapses++;
					if (syn.permanence > permanenceThreshold) {
						activeProximalSynapses++;
					}
				}
			}
		}
		for (Segment seg: cell.distalSegments)  {
			distalSegments++;
			for (Synapse syn: seg.synapses) {
				if (syn.permanence>0) {
					distalSynapses++;
					if (syn.permanence > permanenceThreshold) {
						activeDistalSynapses++;
					}
				}
			}
		}
		for (Segment seg: cell.apicalSegments)  {
			apicalSegments++;
			for (Synapse syn: seg.synapses) {
				if (syn.permanence>0) {
					apicalSynapses++;
					if (syn.permanence > permanenceThreshold) {
						activeApicalSynapses++;
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		r.append("- Cells             : ");
		r.append(cells);
		if (proximalSegments>0) {
			r.append("\n");
			r.append("- Proximal segments : ");
			r.append(proximalSegments);
			r.append("\n");
			r.append("- Proximal synapses : ");
			r.append(proximalSynapses);		
			r.append(" (active: ");
			r.append(activeProximalSynapses);
			r.append(")");
		}
		if (distalSegments>0) {
			r.append("\n");
			r.append("- Distal segments   : ");
			r.append(distalSegments);
			r.append("\n");
			r.append("- Distal synapses   : ");
			r.append(distalSynapses);		
			r.append(" (active: ");
			r.append(activeDistalSynapses);
			r.append(")");
		}
		if (apicalSegments>0) {
			r.append("\n");
			r.append("- Apical segments   : ");
			r.append(apicalSegments);
			r.append("\n");
			r.append("- Apical synapses   : ");
			r.append(apicalSynapses);		
			r.append(" (active: ");
			r.append(activeApicalSynapses);
			r.append(")");
		}
		return r.toString();
	}
}
