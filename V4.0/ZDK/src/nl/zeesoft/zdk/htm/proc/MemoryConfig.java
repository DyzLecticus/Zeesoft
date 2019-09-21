package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;

public class MemoryConfig {
	protected int			length							= 0;
	protected int			bits							= 0;
	
	protected int			depth							= 4;
	protected int			maxDistalConnectionsPerCell		= 10000;
	protected int			initialDistalConnectedRadius	= 20;
	
	protected float			connectionThreshold				= 0.2F;
	protected float			connectionDecrement				= 0.003F;
	protected float			connectionIncrement				= 0.1F;

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
		this.depth = depth;
	}

	public void setMaxDistalConnectionsPerCell(int maxDistalConnectionsPerCell) {
		this.maxDistalConnectionsPerCell = maxDistalConnectionsPerCell;
	}

	public void setInitialDistalConnectedRadius(int initialDistalConnectedRadius) {
		this.initialDistalConnectedRadius = initialDistalConnectedRadius;
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
		if (sizeX * sizeY == length) {
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
	
	public SDR getNewSDR() {
		return new SDR(length);
	}
	
	public SDRMap getNewSDRMap() {
		return new SDRMap(length,bits);
	}
	
	public SDRMap getNewSDRMapWithoutIndex() {
		return new SDRMap(length,bits,false);
	}
}
