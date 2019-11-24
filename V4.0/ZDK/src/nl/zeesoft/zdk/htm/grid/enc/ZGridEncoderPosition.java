package nl.zeesoft.zdk.htm.grid.enc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.GridEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;

public class ZGridEncoderPosition extends ZGridColumnEncoder {
	private int				length				= 256;
	private int				sizeX				= 100;
	private int				sizeY				= 100;
	private int				sizeZ				= 100;
	private float			resolution			= 1;
	
	public ZGridEncoderPosition(int length) {
		this.length = length;
		rebuildEncoder();
	}
	
	@Override
	public String getValueKey() {
		return "POSITION";
	}
	
	public void setDimensions(int sizeX,int sizeY) {
		setDimensions(sizeX,sizeY,0);
	}
	
	public void setDimensions(int sizeX,int sizeY,int sizeZ) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		rebuildEncoder();
	}
	
	public void setResolution(float resolution) {
		this.resolution = resolution;
		rebuildEncoder();
	}
	
	public SDR getSDRForPosition(float[] position) {
		SDR r = null;
		if (sizeZ>0 && position.length==3) {
			r = ((GridEncoder) encoder).getSDRForPosition(position[0],position[1],position[2]);
		} else if (sizeZ<=0 && position.length>=2) {
			r = ((GridEncoder) encoder).getSDRForPosition(position[0],position[1]);
		}
		return r;
	}
	
	protected DateTimeSDR encodeRequestValue(int columnIndex,ZGridRequest request) {
		float[] position = null;
		if (request.inputValues.length>columnIndex &&
			request.inputValues[columnIndex]!=null &&
			request.inputValues[columnIndex] instanceof float[]
			) {
			position = (float[]) request.inputValues[columnIndex];
		}
		if (position==null) {
			if (sizeZ>0) {
				position = new float[3];
			} else {
				position = new float[2];
			}
			for (int i = 0; i < position.length; i++) {
				position[i] = 0;
			}
		}
		DateTimeSDR r = new DateTimeSDR(getSDRForPosition(position));
		r.dateTime = request.dateTime;
		ZStringBuilder pos = new ZStringBuilder();
		pos.append("" + position[0]);
		pos.append("|");
		pos.append("" + position[1]);
		if (position.length>2) {
			pos.append("|");
			pos.append("" + position[2]);
		}
		r.keyValues.put(getValueKey(),pos.toString());
		return r;
	}
	
	protected void rebuildEncoder() {
		GridEncoder enc = null; 
		if (sizeZ>0) {
			enc = GridEncoder.getNewScaled3DGridEncoder(length, sizeX, sizeY, sizeZ);
		} else {
			enc = GridEncoder.getNewScaled2DGridEncoder(length, sizeX, sizeY);
		}
		if (resolution!=1) {
			enc.setResolution(resolution);
		}
		encoder = enc;
	}
}
