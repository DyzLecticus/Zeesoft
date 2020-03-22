package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

public class MemorColumn {
	protected int			index		= 0;
	protected int			posX		= 0;
	protected int			posY		= 0;
	protected MemorCell[]	cells		= null;
	
	protected MemorColumn(int index, int posX, int posY, int depth) {
		this.index = index;
		this.posX = posX;
		this.posY = posY;
		cells = new MemorCell[depth];
	}
	
	protected String getId() {
		return posX + "-" + posY;
	}
	
	protected void destroy() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].destroy();
		}
	}

	protected List<MemorCell> getCellsAsList() {
		List<MemorCell> r = new ArrayList<MemorCell>();
		for (int z = 0; z < cells.length; z++) {
			r.add(cells[z]);
		}
		return r;
	}
}
