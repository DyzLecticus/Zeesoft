package nl.zeesoft.zdk.htm.grid.enc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.GridEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.htm.util.SDR;

public class ZGridEncoderPosition extends ZGridColumnEncoder {
	private GridEncoder		encoder				= null;

	private int				length				= 256;
	private int				sizeX				= 100;
	private int				sizeY				= 100;
	private int				sizeZ				= 100;
	
	public ZGridEncoderPosition(int length) {
		this.length = length;
		rebuildEncoder();
	}

	@Override
	public int length() {
		return encoder.length();
	}
	
	public void setDimensions(int sizeX,int sizeY,int sizeZ) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		rebuildEncoder();
	}
	
	public ZStringBuilder testScalarOverlap() {
		return encoder.testScalarOverlap();
	}
	
	public SDR getSDRForPosition(int[] position) {
		SDR r = null;
		if (sizeZ>0 && position.length==3) {
			r = encoder.getSDRForPosition((float) position[0],(float) position[1],(float) position[2]);
		} else if (sizeZ<=0 && position.length>=2) {
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
			if (sizeZ>0) {
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
		if (sizeZ>0) {
			encoder = GridEncoder.getNewScaled3DGridEncoder(length, sizeX, sizeY, sizeZ);
		} else {
			encoder = GridEncoder.getNewScaled2DGridEncoder(length, sizeX, sizeY);
		}
	}
}
