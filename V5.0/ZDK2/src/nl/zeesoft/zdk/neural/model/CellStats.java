package nl.zeesoft.zdk.neural.model;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.neural.processor.CellsProcessor;

public class CellStats {
	public int					cells			= 0;
	
	public CellSegmentStats		proximalStats	= new CellSegmentStats("Proximal");
	public CellSegmentStats		distalStats		= new CellSegmentStats("Distal");
	public CellSegmentStats		apicalStats		= new CellSegmentStats("Apical");
	
	public CellStats() {
		
	}
	
	public CellStats(CellsProcessor cp) {
		addModelCells(this,cp.getCells(),cp.isCellModel());
	}
	
	public void addStats(CellStats s) {
		this.cells += s.cells;
		this.proximalStats.addStats(s.proximalStats);
		this.distalStats.addStats(s.distalStats);
		this.apicalStats.addStats(s.apicalStats);
	}
	
	public void addModelCells(Object caller, Cells modelCells, boolean isCellModel) {
		Function function = new Function() {
			@Override
			protected Object exec() {
				addCell((Cell) param2, modelCells.config.permanenceThreshold, isCellModel);
				return param2;
			}
		};
		modelCells.applyFunction(caller, function);
	}
	
	public void addCell(Cell cell, float permanenceThreshold, boolean isCellModel) {
		if (isCellModel) {
			cells++;
		}
		proximalStats.addSegments(cell.proximalSegments.segments, permanenceThreshold);
		distalStats.addSegments(cell.distalSegments.segments, permanenceThreshold);
		apicalStats.addSegments(cell.apicalSegments.segments, permanenceThreshold);
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		r.append("Cells: ");
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
