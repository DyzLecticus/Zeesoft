package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MemoryColumn {
	protected	MemoryColumnGroup		columnGroup	= null;	
	protected	int						index		= 0;
	
	protected	List<MemoryColumnCell>	cells		= new ArrayList<MemoryColumnCell>();
	
	protected MemoryColumn(MemoryColumnGroup columnGroup,int index) {
		this.columnGroup = columnGroup;
		this.index = index;
	}
	
	protected void randomizeConnections() {
		for (MemoryColumnCell cell: cells) {
			cell.randomizeConnections();
		}
	}

	protected boolean activateColumnCells() {
		boolean r = false;
		MemoryColumnCell winner = null;
		for (MemoryColumnCell cell: cells) {
			if (cell.predictive) {
				winner = cell;
				cell.active = true;
			}
		}
		if (winner==null) {
			for (MemoryColumnCell cell: cells) {
				r = true;
				cell.active = true;
			}
		}
		return r;
	}
	
	protected void calculateOverlapScoresForActiveLinks() {
		for (MemoryColumnCell cell: cells) {
			cell.calculateOverlapScoresForActiveLinks();
		}
	}

	protected void predictColumnCells(Set<MemoryColumnCell> predictiveCells) {
		for (MemoryColumnCell cell: cells) {
			cell.active = false;
			cell.predictive = predictiveCells.contains(cell);
		}
	}
}
