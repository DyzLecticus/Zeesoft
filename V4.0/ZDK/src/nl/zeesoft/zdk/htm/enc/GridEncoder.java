package nl.zeesoft.zdk.htm.enc;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.util.SDR;

public class GridEncoder extends CombinedEncoder {
	private int		length		= 0;
	private int		bits		= 0;
	private int[]	dimensions	= null;
	private float	resolution	= 1;
	
	public GridEncoder(int length,int bits,int[] dimensions) {
		initialize(length,bits,dimensions,resolution);
	}
	
	public static GridEncoder getNew2DGridEncoder(int length,int bits, int sizeX, int sizeY) {
		int[] dims = new int[2];
		dims[0] = sizeX;
		dims[1] = sizeY;
		return new GridEncoder(length,bits,dims);
	}
	
	public static GridEncoder getNew3DGridEncoder(int length,int bits, int sizeX, int sizeY, int sizeZ) {
		int[] dims = new int[3];
		dims[0] = sizeX;
		dims[1] = sizeY;
		dims[2] = sizeZ;
		return new GridEncoder(length,bits,dims);
	}

	public SDR getSDRForPosition(float posX, float posY) {
		return getSDRForPosition(posX,posY,0);
	}
	
	public SDR getSDRForPosition(float posX, float posY, float posZ) {
		SortedMap<String,Float> values = new TreeMap<String,Float>();
		values.put("0",posX);
		values.put("1",posY);
		values.put("2",posZ);
		return getSDRForValues(values);
	}
	
	public void setResolution(float resolution) {
		this.resolution = resolution;
		initialize(length,bits,dimensions,resolution);
	}
	
	protected void initialize(int length,int bits,int[] dimensions,float resolution) {
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
		int total = 0;
		for (int i = 0; i < dimensions.length; i++) {
			total += dimensions[i];
		}
		for (int i = 0; i < dimensions.length; i++) {
			float factor = (float)dimensions[i] / (float)total;
			int bts = (int)((float)bits * factor);
			if (bts % 2 == 1) {
				bts = bts + 1;
			}
			int len = bts * (length / bits);
			GridDimensionEncoder enc = new GridDimensionEncoder(len,bts);
			enc.setResolution(resolution);
			addEncoder("" + i, enc);
		}
	}
}
