package nl.zeesoft.zdk.neural.model;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.neural.processor.sp.SpatialPooler;
import nl.zeesoft.zdk.neural.processor.tm.TemporalMemory;

public class CellStats {
	public int					cells			= 0;
	
	public CellSegmentStats		proximalStats	= new CellSegmentStats("Proximal");
	public CellSegmentStats		distalStats		= new CellSegmentStats("Distal");
	public CellSegmentStats		apicalStats		= new CellSegmentStats("Apical");
	
	public CellStats() {
		
	}
	
	public CellStats(SpatialPooler sp) {
		addModelCells(this,sp.getCells());
	}
	
	public CellStats(TemporalMemory tm) {
		addModelCells(this,tm.getCells());
	}
	
	public void addStats(CellStats s) {
		this.cells += s.cells;
		this.proximalStats.addStats(s.proximalStats);
		this.distalStats.addStats(s.distalStats);
		this.apicalStats.addStats(s.apicalStats);
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
		proximalStats.addSegments(cell.proximalSegments.segments, permanenceThreshold);
		distalStats.addSegments(cell.distalSegments.segments, permanenceThreshold);
		apicalStats.addSegments(cell.apicalSegments.segments, permanenceThreshold);
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		r.append("- Cells: ");
		r.append(cells);
		if (proximalStats.segments>0) {
			r.append("\n");
			r.append(proximalStats);
		}
		if (distalStats.segments>0) {
			r.append("\n");
			r.append(distalStats);
		}
		if (apicalStats.segments>0) {
			r.append("\n");
			r.append(apicalStats);
		}
		return r.toString();
	}
}
