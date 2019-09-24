package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Column extends ModelObject {
	protected ModelConfig	config				= null;
	
	public int				index				= 0;
	public int				posX				= 0;
	public int				posY				= 0;
	
	public ColumnGroup		columnGroup			= null;
	public List<Cell>		cells				= new ArrayList<Cell>();
	public ProximalDendrite	proximalDendrite	= null;
	
	// Used for boosting by SpatialPooler
	public Queue<Boolean>	activityLog			= new LinkedList<Boolean>();
	public float			totalActive			= 0;
	public float			averageActivity		= 0;
	public float			boostFactor			= 1;
	
	public Column(ModelConfig config, int index,int posX,int posY) {
		this.config = config;
		this.index = index;
		this.posX = posX;
		this.posY = posY;
	}

	public Column copy(boolean includeProximalDendrite,boolean includeCells) {
		Column copy = copy();
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
		return copy;
	}
	
	@Override
	public Column copy() {
		Column copy = new Column(config,index,posX,posY);
		copy.setId(getId());
		return copy;
	}
	
	public List<Integer> calculateInputIndices(Column column) {
		List<Integer> r = new ArrayList<Integer>();
		int inputPosX = column.getInputPosX();
		int inputPosY = column.getInputPosY();
		
		int minPosX = inputPosX - config.proximalRadius;
		int minPosY = inputPosY - config.proximalRadius;
		int maxPosX = inputPosX + 1 + config.proximalRadius;
		int maxPosY = inputPosY + 1 + config.proximalRadius;
		
		if (minPosX < 0) {
			minPosX = 0;
		}
		if (minPosY < 0) {
			minPosY = 0;
		}
		if (maxPosX > config.inputSizeX) {
			maxPosX = config.inputSizeX;
		}
		if (maxPosY > config.inputSizeY) {
			maxPosY = config.inputSizeY;
		}
		
		int posX = 0;
		int posY = 0;
		for (int i = 0; i < config.inputLength; i++) {
			if (posX>=minPosX && posX<maxPosX && posY>=minPosY && posY<maxPosY) {
				r.add(i);
			}
			posX++;
			if (posX % config.inputSizeX == 0) {
				posX = 0;
				posY++;
			}
			if (posY>maxPosY) {
				break;
			}
		}
		return r;
	}

	public int getInputPosX() {
		return getRelativePos(getFloatPosX(),config.proximalRadius,config.inputSizeX);
	}
	
	public int getInputPosY() {
		return getRelativePos(getFloatPosY(),config.proximalRadius,config.inputSizeY);
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
