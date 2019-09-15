package nl.zeesoft.zdk.htm.pool;

import nl.zeesoft.zdk.ZStringBuilder;

public class PoolerConfig {
	protected int			inputSize						= 0;
	protected int			outputSize						= 0;
	protected int			outputBits						= 0;
	protected int			outputDepth						= 0;
	
	protected float			potentialProximalConnections	= 0.50F;
	protected float			potentialDistalConnections		= 0.50F;
	protected boolean		distalColumnGroupGlobal			= false;
	
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
	
	public PoolerConfig(int inputSize, int outputSize, int outputBits, int outputDepth) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.outputBits = outputBits;
		this.outputDepth = outputDepth;
		calculateDimensions();
	}

	public void setPotentialProximalConnections(float potentialConnections) {
		this.potentialProximalConnections = potentialConnections;
	}

	public void setPotentialDistalConnections(float potentialConnections) {
		this.potentialDistalConnections = potentialConnections;
	}

	public void setConnectionThreshold(float connectionThreshold) {
		this.connectionThreshold = connectionThreshold;
	}

	public void setDistalColumnGroupGlobal(boolean distalColumnGroupGlobal) {
		this.distalColumnGroupGlobal = distalColumnGroupGlobal;
	}

	public void setConnectionDecrement(float connectionDecrement) {
		this.connectionDecrement = connectionDecrement;
	}

	public void setConnectionIncrement(float connectionIncrement) {
		this.connectionIncrement = connectionIncrement;
	}

	public void setInputRadius(int inputRadius) {
		this.inputRadius = inputRadius;
	}

	public void setOutputRadius(int outputRadius) {
		this.outputRadius = outputRadius;
	}

	public void setBoostStrength(float boostStrength) {
		this.boostStrength = boostStrength;
	}

	public void setMaxActivityLogSize(int maxActivityLogSize) {
		this.maxActivityLogSize = maxActivityLogSize;
	}
	
	public void setInputDimensions(int sizeX,int sizeY) {
		if (sizeX * sizeY == inputSize) {
			inputSizeX = sizeX;
			inputSizeY = sizeY;
		}
	}
	
	public void setOutputDimensions(int sizeX,int sizeY) {
		if (sizeX * sizeY == outputSize) {
			outputSizeX = sizeX;
			outputSizeY = sizeY;
		}
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Input dimensions: ");
		r.append("" + inputSizeX);
		r.append("x");
		r.append("" + inputSizeY);
		r.append(", output dimensions: ");
		r.append("" + outputSizeX);
		r.append("x");
		r.append("" + outputSizeY);
		return r;
	}

	protected void calculateDimensions() {
		inputSizeX = (int) Math.sqrt(inputSize);
		inputSizeY = inputSizeX;
		if (inputSizeX * inputSizeY < inputSize) {
			inputSizeX += 1;
		}
		if (inputSizeX * inputSizeY < inputSize) {
			inputSizeY += 1;
		}
		outputSizeX = (int) Math.sqrt(outputSize);
		outputSizeY = outputSizeX;
		if (outputSizeX * outputSizeY < outputSize) {
			outputSizeX += 1;
		}
		if (outputSizeX * outputSizeY < outputSize) {
			outputSizeY += 1;
		}
	}
}
