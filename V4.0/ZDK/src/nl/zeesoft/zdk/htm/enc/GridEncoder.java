package nl.zeesoft.zdk.htm.enc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A GridEncoder can be used to encode locations within a multidimensional grid.
 */
public class GridEncoder extends CombinedEncoder {
	private int		length		= 0;
	private int		bits		= 0;
	private int[]	dimensions	= null;
	private float	resolution	= 1;
	private boolean scaled		= false;
	
	public GridEncoder(int length,int bits,int[] dimensions) {
		initialize(length,bits,dimensions,false);
	}

	public GridEncoder(int length,int[] dimensions) {
		initialize(length,length/8,dimensions,true);
	}

	/**
	 * Returns a new 2 dimensional grid encoder.
	 * 
	 * @param length The length
	 * @param bits The number of on bits
	 * @param sizeX The size of the first dimension
	 * @param sizeY The size of the second dimension
	 * @return A new 2 dimensional grid encoder
	 */
	public static GridEncoder getNew2DGridEncoder(int length,int bits, int sizeX, int sizeY) {
		return getNew2DGridEncoder(length,bits,sizeX,sizeY,false);
	}
	
	/**
	 * Returns a new scaled 2 dimensional grid encoder.
	 * 
	 * @param length The length
	 * @param sizeX The size of the first dimension
	 * @param sizeY The size of the second dimension
	 * @return A new scaled 2 dimensional grid encoder
	 */
	public static GridEncoder getNewScaled2DGridEncoder(int length,int sizeX, int sizeY) {
		return getNew2DGridEncoder(length,length / 8,sizeX,sizeY,true);
	}
	
	/**
	 * Returns a new 3 dimensional grid encoder.
	 * 
	 * @param length The length
	 * @param bits The number of on bits
	 * @param sizeX The size of the first dimension
	 * @param sizeY The size of the second dimension
	 * @param sizeZ The size of the third dimension
	 * @return A new 3 dimensional grid encoder
	 */
	public static GridEncoder getNew3DGridEncoder(int length,int bits, int sizeX, int sizeY, int sizeZ) {
		return getNew3DGridEncoder(length,bits,sizeX,sizeY,sizeZ,false);
	}
	
	/**
	 * Returns a new scaled 3 dimensional grid encoder.
	 * 
	 * @param length The length
	 * @param sizeX The size of the first dimension
	 * @param sizeY The size of the second dimension
	 * @param sizeZ The size of the third dimension
	 * @return A new scaled 3 dimensional grid encoder
	 */
	public static GridEncoder getNewScaled3DGridEncoder(int length, int sizeX, int sizeY, int sizeZ) {
		return getNew3DGridEncoder(length,length / 8,sizeX,sizeY,sizeZ,true);
	}
	
	/**
	 * Sets the resolution of the encoder.
	 * 
	 * @param resolution The resolution
	 */
	public void setResolution(float resolution) {
		this.resolution = resolution;
		initialize(length,bits,dimensions,scaled);
	}
	
	/**
	 * Indicates dimensions will be scaled.
	 * Scaled dimensions have a lower capacity but they represent value changes more accurately.
	 * 
	 * @param scaled Indicates dimensions will be scaled
	 */
	public void setScaled(boolean scaled) {
		this.scaled = scaled;
		initialize(length,bits,dimensions,scaled);
	}

	/**
	 * Returns the encoded SDR for a certain 2 dimensional location.
	 * 
	 * @param posX The position in the first dimension (0 - dimension.getCapacity())
	 * @param posY The position in the second dimension (0 - dimension.getCapacity())
	 * @return The encoded SDR
	 */
	public SDR getSDRForPosition(float posX, float posY) {
		return getSDRForPosition(posX,posY,0);
	}
	
	/**
	 * Returns the encoded SDR for a certain 3 dimensional location.
	 * 
	 * @param posX The position in the first dimension (0 - dimension.getCapacity())
	 * @param posY The position in the second dimension (0 - dimension.getCapacity())
	 * @param posZ The position in the third dimension (0 - dimension.getCapacity())
	 * @return The encoded SDR
	 */
	public SDR getSDRForPosition(float posX, float posY, float posZ) {
		SortedMap<String,Float> values = new TreeMap<String,Float>();
		values.put(getNameForDimension(0),posX);
		values.put(getNameForDimension(1),posY);
		values.put(getNameForDimension(2),posZ);
		return getSDRForValues(values);
	}
	
	protected void initialize(int length,int bits,int[] dimensions,boolean scaled) {
		super.initialize();
		if (length < 2) {
			length = 2;
		}
		if (bits < 2) {
			bits = 2;
		}
		this.length = length;
		this.bits = bits;
		this.dimensions = dimensions;
		this.scaled = scaled;

		SortedMap<Integer,List<Integer>> dimensionsBySize = new TreeMap<Integer,List<Integer>>();
		for (int i = 0; i < dimensions.length; i++) {
			List<Integer> list = dimensionsBySize.get(dimensions[i]);
			if (list==null) {
				list = new ArrayList<Integer>();
				dimensionsBySize.put(dimensions[i],list);
			}
			list.add(i);
		}

		int totalScale = 0;
		int[] scales = new int[dimensions.length];
		int scale = 3;
		for (Entry<Integer,List<Integer>> entry: dimensionsBySize.entrySet()) {
			for (Integer dimension: entry.getValue()) {
				scales[dimension] = scale;
				totalScale += scale;
			}
			scale++;
		}
		
		int totalLength = 0;
		for (int i = 0; i < scales.length; i++) {
			float factor = (float)scales[i] / (float)totalScale;
			int bts = (int)((float)bits * factor);
			if (bts % 2 == 1) {
				bts = bts + 1;
			}
			int len = bts * (length / bits);
			
			if (i==scales.length - 1 && totalLength + len < length) {
				len += length - (totalLength + len);
			}
			totalLength += len;
			if (totalLength>length) {
				len -= (totalLength - length);
			}
			if (len > bits) {
				GridDimensionEncoder enc = null;
				if (scaled) {
					enc = new GridDimensionScaledEncoder(len);
				} else {
					enc = new GridDimensionEncoder(len,bts);
				}
				enc.setResolution(resolution);
				addEncoder(getNameForDimension(i),enc);
			}
		}
	}
	
	protected String getNameForDimension(int index) {
		return "D" + String.format("%02d",index + 1);
	}
	
	private GridEncoder(int length,int bits,int[] dimensions,boolean scaled) {
		initialize(length,bits,dimensions,scaled);
	}
	
	private static GridEncoder getNew2DGridEncoder(int length,int bits, int sizeX, int sizeY,boolean scaled) {
		int[] dims = new int[2];
		dims[0] = sizeX;
		dims[1] = sizeY;
		return new GridEncoder(length,bits,dims,scaled);
	}
	
	private static GridEncoder getNew3DGridEncoder(int length,int bits, int sizeX, int sizeY, int sizeZ,boolean scaled) {
		int[] dims = new int[3];
		dims[0] = sizeX;
		dims[1] = sizeY;
		dims[2] = sizeZ;
		return new GridEncoder(length,bits,dims,scaled);
	}
}
