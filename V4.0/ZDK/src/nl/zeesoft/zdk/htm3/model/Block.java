package nl.zeesoft.zdk.htm3.model;

import java.util.ArrayList;
import java.util.List;

public class Block {
	protected final static int		MAX_DENDRITES_PER_CELL		= 200;
	protected final static int		MAX_SYNAPSES_PER_DENDRITE	= 200;
	
	protected List<Column>			columns						= new ArrayList<Column>();
	
	public void initialize(int sizeX, int sizeY, int sizeZ) {
		int cellIndex = 0;
		for (int posX = 0; posX < sizeX; posX++) {
			for (int posY = 0; posY < sizeY; posY++) {
				Column column = new Column(columns.size(),posX,posY);
				columns.add(column);
				for (int posZ = 0; posZ < sizeZ; posZ++) {
					Cell cell = new Cell(cellIndex,posX,posY,posZ);
					column.cells.add(cell);
					cellIndex++;
				}
			}
		}
	}
	
	protected Column getColumnById(String id) {
		Column r = null;
		for (Column column: columns) {
			if (column.getId().equals(id)) {
				r = column;
				break;
			}
		}
		return r;
	}
	
	protected Cell getCellById(String id) {
		Cell r = null;
		String[] elems = id.split("-");
		if (elems.length==3) {
			String columnId = elems[0] + "-" + elems[1];
			Column column = getColumnById(columnId);
			int posZ = Integer.parseInt(elems[2]);
			if (column!=null && column.cells.size()>posZ) {
				r = column.cells.get(posZ);
			}
		}
		return r;
	}
}
