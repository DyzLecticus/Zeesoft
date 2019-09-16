package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;

public class MemoryColumn {
	private		MemoryConfig			config		= null;
	protected	MemoryColumnGroup		columnGroup	= null;	
	protected	int						index		= 0;
	
	protected	List<MemoryColumnCell>	cells		= new ArrayList<MemoryColumnCell>();
	
	protected MemoryColumn(MemoryConfig config,MemoryColumnGroup columnGroup,int index) {
		this.config = config;
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
}
