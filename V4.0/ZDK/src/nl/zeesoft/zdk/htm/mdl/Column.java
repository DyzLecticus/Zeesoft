package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.List;

public class Column extends ModelObject {
	protected ModelConfig	config				= null;
	
	public int				index				= 0;
	public int				posX				= 0;
	public int				posY				= 0;
	
	public ColumnGroup		columnGroup			= null;
	public List<Cell>		cells				= new ArrayList<Cell>();
	public ProximalDendrite	proximalDendrite	= null;
	
	public Column(ModelConfig config, int index,int posX,int posY) {
		this.config = config;
		this.index = index;
		this.posX = posX;
		this.posY = posY;
	}

	public Column copy(boolean includeProximalDendrite,boolean includeCells) {
		Column copy = copy();
		copyTo(copy,includeProximalDendrite,includeCells);
		return copy;
	}
	
	public void copyTo(Column copy,boolean includeProximalDendrite,boolean includeCells) {
		copy.setId(getId());
		copy.columnGroup = columnGroup;
		if (includeProximalDendrite) {
			copy.proximalDendrite = proximalDendrite.copy();
		}
		if (includeCells) {
			for (Cell cell: cells) {
				Cell cellCopy = cell.copy();
				cell.proximalDendrites.add(copy.proximalDendrite);
				copy.cells.add(cellCopy);
			}
		}
	}
	
	@Override
	public Column copy() {
		Column copy = new Column(config,index,posX,posY);
		copy.setId(getId());
		return copy;
	}
	
	public int getColumnPosX() {
		return getRelativePos(getFloatPosX(),config.distalRadius,config.columnSizeX);
	}
	
	public int getColumnPosY() {
		return getRelativePos(getFloatPosY(),config.distalRadius,config.columnSizeY);
	}

	public float getFloatPosX() {
		return (float) posX / (float) config.columnSizeX;  
	}
	
	public float getFloatPosY() {
		return (float) posY / (float) config.columnSizeY;  
	}

	public static int getRelativePos(float floatPos,int radius,int size) {
		int r = 0;
		int min = radius;
		int max = size - radius;
		if (min>=max) {
			r = size / 2;
		} else {
			max = max - radius;
			r = min + ((int) (floatPos * (float) max));
		}
		return r;
	}
}
