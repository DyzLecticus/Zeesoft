package nl.zeesoft.zdk.neural.sp;

import nl.zeesoft.zdk.matrix.Size;

public class SpConfig {
	public Size		inputSize					= new Size(16,16);
	public Size		outputSize					= new Size(48,48);
	public int		outputOnBits				= 46;

	public float	permanenceThreshold			= 0.1F;
	public float	permanenceIncrement			= 0.05F;
	public float	permanenceDecrement			= 0.008F;

	public boolean	learn						= true;
	
	public float	potentialConnections		= 0.85F;
	public float	potentialRadius				= 2;
	
	public int		activationHistorySize		= 1000;
	public int		boostFactorPeriod			= 10;
	public int		boostStrength				= 2;
	
	public SpConfig copy() {
		SpConfig r = new SpConfig();
		r.inputSize = inputSize.copy();
		r.outputSize = outputSize.copy();
		r.outputOnBits = outputOnBits;
		r.learn = learn;
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
