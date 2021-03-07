package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.matrix.Size;

public class SpatialPoolerConfig {
	public Size		inputSize					= new Size(3,3);
	public Size		outputSize					= new Size(10,10);
	public int		outputOnBits				= 2;

	public float	permanenceThreshold			= 0.1F;
	public float	permanenceIncrement			= 0.05F;
	public float	permanenceDecrement			= 0.008F;

	public float	potentialConnections		= 0.85F;
	public float	potentialRadius				= 16;
	
	public int		activationHistorySize		= 1000;
	public int		boostFactorPeriod			= 10;
	public int		boostStrength				= 2;
	
	public SpatialPoolerConfig copy() {
		SpatialPoolerConfig r = new SpatialPoolerConfig();
		r.inputSize = inputSize.copy();
		r.outputSize = outputSize.copy();
		r.outputOnBits = outputOnBits;
		r.permanenceThreshold = permanenceThreshold;
		r.permanenceIncrement = permanenceIncrement;
		r.permanenceDecrement = permanenceDecrement;
		r.potentialConnections = potentialConnections;
		r.potentialRadius = potentialRadius;
		r.activationHistorySize = activationHistorySize;
		r.boostFactorPeriod = boostFactorPeriod;
		r.boostStrength = boostStrength;
		return r;
	}
}
