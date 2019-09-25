package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;

public class PoolerConfig {
	protected boolean		initialized						= false;
	
	protected int			inputLength						= 0;
	protected int			outputLength					= 0;
	protected int			outputBits						= 0;
	
	protected float			potentialProximalConnections	= 0.5F;
	
	protected float			connectionThreshold				= 0.1F;
	protected float			connectionDecrement				= 0.008F;
	protected float			connectionIncrement				= 0.05F;
	
	protected int			inputRadius						= 5;
	protected int			outputRadius					= 10;

	protected float			boostStrength					= 10;
	protected int			maxActivityLogSize				= 100;

	protected int			inputSizeX						= 0;
	protected int			inputSizeY						= 0;
	protected int			outputSizeX						= 0;
	protected int			outputSizeY						= 0;
	
	public PoolerConfig(int inputLength, int outputLength, int outputBits) {
		this.inputLength = inputLength;
		this.outputLength = outputLength;
		this.outputBits = outputBits;
		calculateDimensions();
	}

	public void setPotentialProximalConnections(float potentialConnections) {
		if (!initialized) {
			this.potentialProximalConnections = potentialConnections;
		}
	}

	public void setConnectionThreshold(float connectionThreshold) {
		if (!initialized) {
			this.connectionThreshold = connectionThreshold;
		}
	}

	public void setConnectionDecrement(float connectionDecrement) {
		if (!initialized) {
			this.connectionDecrement = connectionDecrement;
		}
	}

	public void setConnectionIncrement(float connectionIncrement) {
		if (!initialized) {
			this.connectionIncrement = connectionIncrement;
		}
	}

	public void setInputRadius(int inputRadius) {
		if (!initialized) {
			this.inputRadius = inputRadius;
		}
	}

	public void setOutputRadius(int outputRadius) {
		if (!initialized) {
			this.outputRadius = outputRadius;
		}
	}

	public void setBoostStrength(float boostStrength) {
		if (!initialized) {
			this.boostStrength = boostStrength;
		}
	}

	public void setMaxActivityLogSize(int maxActivityLogSize) {
		if (!initialized) {
			this.maxActivityLogSize = maxActivityLogSize;
		}
	}
	
	public void setInputDimensions(int sizeX,int sizeY) {
		if (!initialized && sizeX * sizeY == inputLength) {
			inputSizeX = sizeX;
			inputSizeY = sizeY;
		}
	}
	
	public void setOutputDimensions(int sizeX,int sizeY) {
		if (!initialized && sizeX * sizeY == outputLength) {
			outputSizeX = sizeX;
			outputSizeY = sizeY;
		}
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Pooler input dimensions: ");
		r.append("" + inputSizeX);
		r.append("x");
		r.append("" + inputSizeY);
		r.append(", output dimensions: ");
		r.append("" + outputSizeX);
		r.append("x");
		r.append("" + outputSizeY);
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
		inputSizeX = (int) Math.sqrt(inputLength);
		inputSizeY = inputSizeX;
		if (inputSizeX * inputSizeY < inputLength) {
			inputSizeX += 1;
		}
		if (inputSizeX * inputSizeY < inputLength) {
			inputSizeY += 1;
		}
		outputSizeX = (int) Math.sqrt(outputLength);
		outputSizeY = outputSizeX;
		if (outputSizeX * outputSizeY < outputLength) {
			outputSizeX += 1;
		}
		if (outputSizeX * outputSizeY < outputLength) {
			outputSizeY += 1;
		}
	}
}
