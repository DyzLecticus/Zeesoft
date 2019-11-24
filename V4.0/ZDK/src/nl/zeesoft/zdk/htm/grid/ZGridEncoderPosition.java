package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.GridEncoder;
import nl.zeesoft.zdk.htm.util.SDR;

public class ZGridEncoderPosition extends ZGridColumnEncoder {
	private GridEncoder		encoder				= null;

	private int				length				= 256;
	private int				sizeX				= 100;
	private int				sizeY				= 100;
	private int				sizeZ				= 100;
	
	private boolean 		threeDimensional	= true;
	
	public ZGridEncoderPosition(int length,int sizeX,int sizeY,int sizeZ) {
		this.length = length;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		rebuildEncoder();
	}

	public void setThreeDimensional(boolean threeDimensional) {
		this.threeDimensional = threeDimensional;
		rebuildEncoder();
	}
	
	public ZStringBuilder testScalarOverlap() {
		return encoder.testScalarOverlap();
	}
	
	public SDR getSDRForPosition(int[] position) {
		SDR r = null;
		if (threeDimensional && position.length==3) {
			r = encoder.getSDRForPosition((float) position[0],(float) position[1],(float) position[2]);
		} else if (!threeDimensional && position.length>=2) {
			r = encoder.getSDRForPosition((float) position[0],(float) position[1]);
		}
		return r;
	}
	
	protected SDR encodeRequestValue(int columnIndex,ZGridRequest request) {
		int[] position = null;
		if (request.inputValues.length>columnIndex &&
			request.inputValues[columnIndex]!=null &&
			request.inputValues[columnIndex] instanceof int[]
			) {
			position = (int[]) request.inputValues[columnIndex];
		}
		if (position==null) {
			if (threeDimensional) {
				position = new int[3];
			} else {
				position = new int[2];
			}
			for (int i = 0; i < position.length; i++) {
				position[i] = 0;
			}
		}
		return getSDRForPosition(position);
	}
	
	protected void rebuildEncoder() {
		if (threeDimensional) {
			encoder = GridEncoder.getNewScaled3DGridEncoder(length, sizeX, sizeY, sizeZ);
		} else {
			encoder = GridEncoder.getNewScaled2DGridEncoder(length, sizeX, sizeY);
		}
	}
}
