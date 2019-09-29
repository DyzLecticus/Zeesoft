package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;

public class MemoryConfig {
	protected boolean		initialized						= false;
	
	protected int			length							= 0;
	protected int			bits							= 0;
	
	protected int			depth							= 4;
	protected int			maxDistalConnectionsPerCell		= 9999;
	protected int			localDistalConnectedRadius		= 64;
	
	protected float			distalConnectionThreshold		= 0.2F;
	protected float			distalConnectionDecrement		= 0.003F;
	protected float			distalConnectionIncrement		= 0.1F;

	protected int			sizeX							= 0;
	protected int			sizeY							= 0;

	public MemoryConfig(PoolerConfig poolerConfig) {
		this.length = poolerConfig.outputLength;
		this.sizeX = poolerConfig.outputSizeX;
		this.sizeY = poolerConfig.outputSizeY;
		this.bits = poolerConfig.outputBits;
	}

	public MemoryConfig(int length,int bits) {
		this.length = length;
		this.bits = bits;
		calculateDimensions();
	}
	
	public void setDepth(int depth) {
		if (!initialized) {
			this.depth = depth;
		}
	}

	public void setDistalConnectionThreshold(float distalConnectionThreshold) {
		if (!initialized) {
			this.distalConnectionThreshold = distalConnectionThreshold;
		}
	}

	public void setDistalConnectionDecrement(float distalConnectionDecrement) {
		if (!initialized) {
			this.distalConnectionDecrement = distalConnectionDecrement;
		}
	}

	public void setDistalConnectionIncrement(float distalConnectionIncrement) {
		if (!initialized) {
			this.distalConnectionIncrement = distalConnectionIncrement;
		}
	}

	public void setMaxDistalConnectionsPerCell(int maxDistalConnectionsPerCell) {
		if (!initialized) {
			this.maxDistalConnectionsPerCell = maxDistalConnectionsPerCell;
		}
	}

	public void setLocalDistalConnectedRadius(int localDistalConnectedRadius) {
		if (!initialized) {
			this.localDistalConnectedRadius = localDistalConnectedRadius;
		}
	}

	public void setDimensions(int sizeX,int sizeY) {
		if (!initialized && sizeX * sizeY == length) {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Memory dimensions: ");
		r.append("" + sizeX);
		r.append("x");
		r.append("" + sizeY);
		r.append("x");
		r.append("" + depth);
		return r;
	}
	
	public SDR getNewSDR() {
		return new SDR(length);
	}
	
	public SDRMap getNewSDRMap() {
		return new SDRMap(length,bits);
	}
	
	public SDRMap getNewSDRMapWithoutIndex() {
		return new SDRMap(length,bits,false);
	}

	protected void calculateDimensions() {
		sizeX = (int) Math.sqrt(length);
		sizeY = sizeX;
		if (sizeX * sizeY < length) {
			sizeX += 1;
		}
		if (sizeX * sizeY < length) {
			sizeY += 1;
		}
	}
}
