package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A MemoryConfig is used to configure a single temporal memory.
 * The configuration cannot be changed once it has been used to instantiate a temporal memory.
 * The length is automatically translated to a 3D memory space of the specified depth.
 */
public class MemoryConfig extends ProcessorConfigObject {
	protected int			length								= 100;
	protected int			bits								= 2;
	
	protected int			depth								= 4;
	protected int			maxDistalConnectionsPerCell			= 9999;
	protected int			localDistalConnectedRadius			= 64;
	protected int			minAlmostActiveDistalConnections	= 5;
	
	protected float			distalConnectionThreshold			= 0.2F;
	protected float			distalConnectionDecrement			= 0.003F;
	protected float			distalConnectionIncrement			= 0.1F;
	
	protected List<Integer>	contextDimensions					= new ArrayList<Integer>();

	protected int			sizeX								= 0;
	protected int			sizeY								= 0;

	public MemoryConfig() {
		calculateDimensions();
	}

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
	
	@Override
	public MemoryConfig copy() {
		MemoryConfig r = new MemoryConfig(length, bits);
		r.sizeX = sizeX;
		r.sizeY = sizeY;
		r.depth = depth;
		r.maxDistalConnectionsPerCell = maxDistalConnectionsPerCell;
		r.localDistalConnectedRadius = localDistalConnectedRadius;
		r.minAlmostActiveDistalConnections = minAlmostActiveDistalConnections;
		r.distalConnectionThreshold = distalConnectionThreshold;
		r.distalConnectionDecrement = distalConnectionDecrement;
		r.distalConnectionIncrement = distalConnectionIncrement;
		for (Integer length: contextDimensions) {
			r.contextDimensions.add(new Integer(length));
		}
		return r;
	}
	
	/**
	 * Sets the depth of the temporal memory.
	 * A deeper memory can learn more sequence variations.
	 * 
	 * @param depth The depth
	 */
	public void setDepth(int depth) {
		if (!initialized) {
			this.depth = depth;
		}
	}

	/**
	 * Sets the distal connection (permanence) threshold.
	 * 
	 * @param distalConnectionThreshold The distal connection threshold
	 */
	public void setDistalConnectionThreshold(float distalConnectionThreshold) {
		if (!initialized) {
			this.distalConnectionThreshold = distalConnectionThreshold;
		}
	}

	/**
	 * Sets the distal connection (permanence) decrement.
	 * 
	 * @param distalConnectionDecrement The distal connection decrement
	 */
	public void setDistalConnectionDecrement(float distalConnectionDecrement) {
		if (!initialized) {
			this.distalConnectionDecrement = distalConnectionDecrement;
		}
	}


	/**
	 * Sets the distal connection (permanence) increment.
	 * 
	 * @param distalConnectionIncrement The distal connection increment
	 */
	public void setDistalConnectionIncrement(float distalConnectionIncrement) {
		if (!initialized) {
			this.distalConnectionIncrement = distalConnectionIncrement;
		}
	}

	/**
	 * Sets the maximal number of distal connections per cell.
	 * 
	 * @param maxDistalConnectionsPerCell The maximal number of distal connections per cell
	 */
	public void setMaxDistalConnectionsPerCell(int maxDistalConnectionsPerCell) {
		if (!initialized) {
			this.maxDistalConnectionsPerCell = maxDistalConnectionsPerCell;
		}
	}

	/**
	 * Sets the local distal connection radius.
	 * Used to limit the range of distal connections between cells within the temporal memory.
	 * Use a short radius when 3D topology is important.
	 * 
	 * @param localDistalConnectedRadius The local distal connected radius
	 */
	public void setLocalDistalConnectedRadius(int localDistalConnectedRadius) {
		if (!initialized) {
			this.localDistalConnectedRadius = localDistalConnectedRadius;
		}
	}

	/**
	 * Sets the minimal number of almost active distal connections a cell must have to be selected the winner of a bursting column.
	 * 
	 * @param minAlmostActiveDistalConnections The minimal number of almost active distal connections
	 */
	public void setMinAlmostActiveDistalConnections(int minAlmostActiveDistalConnections) {
		if (!initialized) {
			this.minAlmostActiveDistalConnections = minAlmostActiveDistalConnections;
		}
	}

	/**
	 * Adds a context dimension with the specified length.
	 * 
	 * @param length The length of the dimension
	 */
	public void addContextDimension(int length) {
		if (!initialized) {
			this.contextDimensions.add(length);
		}
	}

	/**
	 * Specifies a specific context dimension
	 * 
	 * @param index The index of the dimension to replace
	 * @param length The length of the dimension
	 */
	public void setContextDimensions(int index, int length) {
		if (!initialized) {
			this.contextDimensions.set(index,length);
		}
	}

	/**
	 * Removes all context dimensions.
	 */
	public void clearContextDimensions() {
		if (!initialized) {
			this.contextDimensions.clear();
		}
	}

	/**
	 * Specifies the exact 2 dimensional size of the temporal memory.
	 * 
	 * @param sizeX The x axis size
	 * @param sizeY The y axis size
	 */
	public void setDimensions(int sizeX,int sizeY) {
		if (!initialized && sizeX * sizeY == length) {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}
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

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		
		json.rootElement.children.add(new JsElem("length","" + length));
		json.rootElement.children.add(new JsElem("sizeX","" + sizeX));
		json.rootElement.children.add(new JsElem("sizeY","" + sizeY));
		json.rootElement.children.add(new JsElem("bits","" + bits));
		
		json.rootElement.children.add(new JsElem("depth","" + depth));
		json.rootElement.children.add(new JsElem("maxDistalConnectionsPerCell","" + maxDistalConnectionsPerCell));
		json.rootElement.children.add(new JsElem("localDistalConnectedRadius","" + localDistalConnectedRadius));
		json.rootElement.children.add(new JsElem("minAlmostActiveDistalConnections","" + minAlmostActiveDistalConnections));
		json.rootElement.children.add(new JsElem("distalConnectionThreshold","" + distalConnectionThreshold));
		json.rootElement.children.add(new JsElem("distalConnectionDecrement","" + distalConnectionDecrement));
		json.rootElement.children.add(new JsElem("distalConnectionIncrement","" + distalConnectionIncrement));
		ZStringBuilder cDims = new ZStringBuilder();
		for (Integer length: contextDimensions) {
			if (cDims.length()>0) {
				cDims.append(",");
			}
			cDims.append("" + length);
		}
		json.rootElement.children.add(new JsElem("contextDimensions",cDims,true));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (!initialized && json.rootElement!=null) {
			length = json.rootElement.getChildInt("length",length);
			sizeX = json.rootElement.getChildInt("sizeX",sizeX);
			sizeY = json.rootElement.getChildInt("sizeY",sizeY);
			bits = json.rootElement.getChildInt("bits",bits);

			depth = json.rootElement.getChildInt("depth",depth);
			maxDistalConnectionsPerCell = json.rootElement.getChildInt("maxDistalConnectionsPerCell",maxDistalConnectionsPerCell);
			localDistalConnectedRadius = json.rootElement.getChildInt("localDistalConnectedRadius",localDistalConnectedRadius);
			minAlmostActiveDistalConnections = json.rootElement.getChildInt("minAlmostActiveDistalConnections",minAlmostActiveDistalConnections);
			distalConnectionThreshold = json.rootElement.getChildFloat("distalConnectionThreshold",distalConnectionThreshold);
			distalConnectionDecrement = json.rootElement.getChildFloat("distalConnectionDecrement",distalConnectionDecrement);
			distalConnectionIncrement = json.rootElement.getChildFloat("distalConnectionIncrement",distalConnectionIncrement);
			ZStringBuilder cDims = json.rootElement.getChildZStringBuilder("contextDimensions");
			contextDimensions.clear();
			if (cDims.length()>0) {
				List<ZStringBuilder> pElems = cDims.split(",");
				for (ZStringBuilder pStep: pElems) {
					contextDimensions.add(Integer.parseInt(pStep.toString()));
				}
			}
		}
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

	public int getLength() {
		return length;
	}

	public int getBits() {
		return bits;
	}

	public int getDepth() {
		return depth;
	}

	public int getMaxDistalConnectionsPerCell() {
		return maxDistalConnectionsPerCell;
	}

	public int getLocalDistalConnectedRadius() {
		return localDistalConnectedRadius;
	}

	public int getMinAlmostActiveDistalConnections() {
		return minAlmostActiveDistalConnections;
	}

	public float getDistalConnectionThreshold() {
		return distalConnectionThreshold;
	}

	public float getDistalConnectionDecrement() {
		return distalConnectionDecrement;
	}

	public float getDistalConnectionIncrement() {
		return distalConnectionIncrement;
	}

	public List<Integer> getContextDimensions() {
		return new ArrayList<Integer>(contextDimensions);
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
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
