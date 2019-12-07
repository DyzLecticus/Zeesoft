package nl.zeesoft.zdk.htm.grid.enc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.GridEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ZGridEncoderPosition extends ZGridColumnEncoder {
	private int				length				= 96;
	private int				sizeX				= 100;
	private int				sizeY				= 100;
	private int				sizeZ				= 100;
	private float			resolution			= 1;

	public ZGridEncoderPosition() {
		rebuildEncoder();
	}

	public ZGridEncoderPosition(int length) {
		this.length = length;
		rebuildEncoder();
	}
	
	@Override
	public String getValueKey() {
		return "POSITION";
	}

	public void setLength(int length) {
		this.length = length;
		rebuildEncoder();
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

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("length","" + length));
		json.rootElement.children.add(new JsElem("sizeX","" + sizeX));
		json.rootElement.children.add(new JsElem("sizeY","" + sizeY));
		json.rootElement.children.add(new JsElem("sizeZ","" + sizeZ));
		json.rootElement.children.add(new JsElem("resolution","" + resolution));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			length = json.rootElement.getChildInt("length",length);
			sizeX = json.rootElement.getChildInt("sizeX",sizeX);
			sizeY = json.rootElement.getChildInt("sizeY",sizeY);
			sizeZ = json.rootElement.getChildInt("sizeZ",sizeZ);
			resolution = json.rootElement.getChildFloat("resolution",resolution);
			rebuildEncoder();
		}
	}

	@Override
	protected DateTimeSDR encodeRequestValue(int columnIndex,ZGridResult result) {
		float[] position = null;
		if (result.getRequest().inputValues.length>columnIndex &&
			result.getRequest().inputValues[columnIndex]!=null &&
			result.getRequest().inputValues[columnIndex] instanceof float[]
			) {
			position = (float[]) result.getRequest().inputValues[columnIndex];
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
		r.dateTime = result.getRequest().dateTime;
		ZStringBuilder pos = new ZStringBuilder();
		pos.append("" + position[0]);
		pos.append("|");
		pos.append("" + position[1]);
		if (position.length>2) {
			pos.append("|");
			pos.append("" + position[2]);
		}
		r.keyValues.put(getValueKey(),pos.toString());
		String label = getInputLabel(columnIndex,result);
		if (label.length()>0) {
			r.keyValues.put(DateTimeSDR.LABEL_KEY,label);
		}
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
