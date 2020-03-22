package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.json.JsFile;

public class MemorConfig extends ProcessorConfigObject {
	protected int			length								= 1024;
	protected int			depth								= 4;
	protected int			bits								= 21;
	
	protected int			maxDistalSegmentsPerCell			= 256;
	protected int			maxDistalConnectionsPerSegment		= 256;
	protected int			localDistalConnectedRadius			= 64;
	protected int			maxAddDistalConnections				= 21;
	protected int			minActiveDistalConnections			= 13;
	protected int			minAlmostActiveDistalConnections	= 10;
	
	protected float			distalConnectionThreshold			= 0.2F;
	protected float			distalConnectionDecrement			= 0.13F;
	protected float			distalConnectionIncrement			= 0.7F;
	
	protected int			sizeX								= 16;
	protected int			sizeY								= 16;

	public MemorConfig(int length,int depth,int bits) {
		this.length = length;
		this.depth = depth;
		this.bits = bits;
		calculateDimensions();
	}
	
	public MemorConfig(PoolerConfig poolerConfig) {
		this.length = poolerConfig.outputLength;
		this.sizeX = poolerConfig.outputSizeX;
		this.sizeY = poolerConfig.outputSizeY;
		this.bits = poolerConfig.outputBits;
	}
	
	/**
	 * Returns a description of this configuration.
	 * 
	 * @return A description
	 */
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Memory dimensions: ");
		r.append("" + sizeX);
		r.append("*");
		r.append("" + sizeY);
		r.append("*");
		r.append("" + depth);
		return r;
	}
	
	/**
	 * Returns a new SDR based on the length of the temporal memory.
	 * 
	 * @return A new SDR 
	 */
	public SDR getNewSDR() {
		return new SDR(length);
	}
	
	/**
	 * Returns a new SDR map based on the length and bits of the temporal memory.
	 * 
	 * @return A new SDR map
	 */
	public SDRMap getNewSDRMap() {
		return new SDRMap(length,bits);
	}

	@Override
	public ProcessorConfigObject copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsFile toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromJson(JsFile json) {
		// TODO Auto-generated method stub
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
