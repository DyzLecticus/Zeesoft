package nl.zeesoft.zdk.neural.processors;

public class SpatialPoolerConfig extends SDRProcessorConfig{
	public int		inputSizeX					= 16;
	public int		inputSizeY					= 16;
	
	public int		outputSizeX					= 48;
	public int		outputSizeY					= 48;
	public int		outputOnBits				= 46;
	
	public float	potentialConnections		= 0.85F;
	public int		potentialRadius				= 0;
	
	public float	permanenceThreshold			= 0.1F;
	public float	permanenceIncrement			= 0.05F;
	public float	permanenceDecrement			= 0.008F;
	
	public int		activationHistorySize		= 1000;
	public int		boostStrength				= 2;	
}
