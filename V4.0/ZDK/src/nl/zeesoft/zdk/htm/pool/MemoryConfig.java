package nl.zeesoft.zdk.htm.pool;

import nl.zeesoft.zdk.ZStringBuilder;

public class MemoryConfig {
	protected int			size							= 0;
	
	protected int			depth							= 4;
	protected float			potentialDistalConnections		= 0.50F;
	protected boolean		distalColumnGroupGlobal			= false;
	protected int			maxDistalConnectionsPerCell		= 10000;
	
	protected float			connectionThreshold				= 0.5F;
	protected float			connectionDecrement				= 0.05F;
	protected float			connectionIncrement				= 0.1F;

	protected int			sizeX							= 0;
	protected int			sizeY							= 0;

	public MemoryConfig(PoolerConfig poolerConfig) {
		this.size = poolerConfig.outputSize;
		this.sizeX = poolerConfig.outputSizeX;
		this.sizeY = poolerConfig.outputSizeY;
	}

	public MemoryConfig(int size) {
		this.size = size;
		calculateDimensions();
	}

	public void setPotentialDistalConnections(float potentialConnections) {
		this.potentialDistalConnections = potentialConnections;
	}

	public void setDistalColumnGroupGlobal(boolean distalColumnGroupGlobal) {
		this.distalColumnGroupGlobal = distalColumnGroupGlobal;
	}

	public void setMaxDistalConnectionsPerCell(int maxDistalConnectionsPerCell) {
		this.maxDistalConnectionsPerCell = maxDistalConnectionsPerCell;
	}

	public void setConnectionThreshold(float connectionThreshold) {
		this.connectionThreshold = connectionThreshold;
	}

	public void setConnectionDecrement(float connectionDecrement) {
		this.connectionDecrement = connectionDecrement;
	}

	public void setConnectionIncrement(float connectionIncrement) {
		this.connectionIncrement = connectionIncrement;
	}
	
	public void setDimensions(int sizeX,int sizeY) {
		if (sizeX * sizeY == size) {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Dimensions: ");
		r.append("" + sizeX);
		r.append("x");
		r.append("" + sizeY);
		r.append("x");
		r.append("" + depth);
		return r;
	}

	protected void calculateDimensions() {
		sizeX = (int) Math.sqrt(size);
		sizeY = sizeX;
		if (sizeX * sizeY < size) {
			sizeX += 1;
		}
		if (sizeX * sizeY < size) {
			sizeY += 1;
		}
	}
}
