package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;

public class PoolerConfig {
	protected boolean		initialized						= false;
	
	protected int			inputLength						= 0;
	protected int			outputLength					= 0;
	protected int			outputBits						= 0;
	
	protected float			potentialProximalConnections	= 0.75F;
	protected int			proximalRadius					= 5;
	protected float			proximalConnectionThreshold		= 0.1F;
	protected float			proximalConnectionDecrement		= 0.008F;
	protected float			proximalConnectionIncrement		= 0.05F;
	
	protected int			boostStrength					= 10;
	protected int			boostInhibitionRadius			= 10;
	protected int			boostActivityLogSize			= 100;

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

	public void setPotentialProximalConnections(float potentialProximalConnections) {
		if (!initialized) {
			this.potentialProximalConnections = potentialProximalConnections;
		}
	}

	public void setProximalRadius(int proximalRadius) {
		if (!initialized) {
			this.proximalRadius = proximalRadius;
		}
	}

	public void setProximalConnectionThreshold(float proximalConnectionThreshold) {
		if (!initialized) {
			this.proximalConnectionThreshold = proximalConnectionThreshold;
		}
	}

	public void setProximalConnectionDecrement(float proximalConnectionDecrement) {
		if (!initialized) {
			this.proximalConnectionDecrement = proximalConnectionDecrement;
		}
	}

	public void setProximalConnectionIncrement(float proximalConnectionIncrement) {
		if (!initialized) {
			this.proximalConnectionIncrement = proximalConnectionIncrement;
		}
	}

	public void setBoostStrength(int boostStrength) {
		if (!initialized) {
			this.boostStrength = boostStrength;
		}
	}

	public void setBoostInhibitionRadius(int boostInhibitionRadius) {
		if (!initialized) {
			this.boostInhibitionRadius = boostInhibitionRadius;
		}
	}

	public void setBoostActivityLogSize(int boostActivityLogSize) {
		if (!initialized) {
			this.boostActivityLogSize = boostActivityLogSize;
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
