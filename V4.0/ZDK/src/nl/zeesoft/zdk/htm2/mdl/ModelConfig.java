package nl.zeesoft.zdk.htm2.mdl;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.htm.util.SDRMap;

public class ModelConfig {
	protected boolean		initialized						= false;
	
	protected int			inputLength						= 0;
	protected int			columnDepth						= 0;
	protected int			outputLength					= 0;
	protected int			outputBits						= 0;
	
	protected int			proximalRadius					= 5;
	protected int			distalRadius					= 10;

	protected int			inputSizeX						= 0;
	protected int			inputSizeY						= 0;
	protected int			columnSizeX						= 0;
	protected int			columnSizeY						= 0;
	
	public ModelConfig(int inputLength, int columnDepth, int outputLength, int outputBits) {
		if (inputLength < 100) {
			inputLength = 100;
		}
		if (columnDepth < 1) {
			columnDepth = 1;
		}
		if (outputLength < 100) {
			outputLength = 100;
		}
		if (outputBits < 2) {
			outputBits = 2;
		}
		this.inputLength = inputLength;
		this.columnDepth = columnDepth;
		this.outputLength = outputLength;
		this.outputBits = outputBits;
		calculateDimensions();
	}

	public int getInputLength() {
		return inputLength;
	}

	public int getColumnDepth() {
		return columnDepth;
	}

	public int getOutputLength() {
		return outputLength;
	}

	public int getOutputBits() {
		return outputBits;
	}

	public int getProximalRadius() {
		return proximalRadius;
	}

	public int getDistalRadius() {
		return distalRadius;
	}

	public int getInputSizeX() {
		return inputSizeX;
	}

	public int getInputSizeY() {
		return inputSizeY;
	}

	public int getColumnSizeX() {
		return columnSizeX;
	}

	public int getColumnSizeY() {
		return columnSizeY;
	}

	public void setProximalRadius(int proximalRadius) {
		if (!initialized) {
			this.proximalRadius = proximalRadius;
		}
	}

	public void setDistalRadius(int distalRadius) {
		if (!initialized) {
			this.distalRadius = distalRadius;
		}
	}
	
	public void setInputDimensions(int sizeX,int sizeY) {
		if (!initialized && sizeX * sizeY == inputLength) {
			inputSizeX = sizeX;
			inputSizeY = sizeY;
		}
	}
	
	public void setColumnDimensions(int sizeX,int sizeY,int sizeZ) {
		if (!initialized) {
			if (sizeX * sizeY == outputLength) {
				columnSizeX = sizeX;
				columnSizeY = sizeY;
			}
			columnDepth = sizeZ;
		}
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Model input dimensions: ");
		r.append("" + inputSizeX);
		r.append("x");
		r.append("" + inputSizeY);
		r.append(", column dimensions: ");
		r.append("" + columnSizeX);
		r.append("x");
		r.append("" + columnSizeY);
		r.append("x");
		r.append("" + columnDepth);
		return r;
	}
	
	public SDR getNewSDR() {
		return new SDR(outputLength);
	}
	
	public SDRMap getNewSDRMap() {
		return new SDRMap(outputLength,outputBits);
	}
	
	public SDRMap getNewSDRMapWithoutIndex() {
		return new SDRMap(outputLength,outputBits,false);
	}
	
	protected void calculateDimensions() {
		if (!initialized) {
			inputSizeX = (int) Math.sqrt(inputLength);
			inputSizeY = inputSizeX;
			if (inputSizeX * inputSizeY < inputLength) {
				inputSizeX += 1;
			}
			if (inputSizeX * inputSizeY < inputLength) {
				inputSizeY += 1;
			}
			columnSizeX = (int) Math.sqrt(outputLength);
			columnSizeY = columnSizeX;
			if (columnSizeX * columnSizeY < outputLength) {
				columnSizeX += 1;
			}
			if (columnSizeX * columnSizeY < outputLength) {
				columnSizeY += 1;
			}
		}
	}
}
