package nl.zeesoft.zdk.neural.processors;

/**
 * This class can be used to configure a SpatialPooler before initialization.
 * 
 * Please note; The current implementation does not support local inhibition. 
 * 
 * Configurable properties;
 * - inputSizeX, inputSizeY; Input SDR dimensions
 * - outputSizeX, outputSizeY; Output SDR dimensions (mini column dimensions)
 * - outputOnBits; Maximum number of on bits in the output
 * - potentialConnections, potentialRadius; Number and optional radius of potential connections relative to the input space 
 * - permanenceThreshold, permanenceIncrement, permanenceDecrement; Potential synapse adaptation control 
 * - activationHistorySize; Historic column activation buffer size (used to calculate boost factors) 
 * - boostStrength; Boost strength
 */
public class SpatialPoolerConfig extends SDRProcessorConfig{
	public int		inputSizeX					= 16;
	public int		inputSizeY					= 16;
	
	public int		outputSizeX					= 48;
	public int		outputSizeY					= 48;
	public int		outputOnBits				= 46;
	
	public float	potentialConnections		= 0.85F;
	public int		potentialRadius				= 16;
	
	public float	permanenceThreshold			= 0.1F;
	public float	permanenceIncrement			= 0.05F;
	public float	permanenceDecrement			= 0.008F;
	
	public int		activationHistorySize		= 1000;
	public int		boostStrength				= 2;
}
