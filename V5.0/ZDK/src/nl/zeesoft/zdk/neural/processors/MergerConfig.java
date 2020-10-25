package nl.zeesoft.zdk.neural.processors;

/**
 * This class can be used to configure a Merger before initialization.
 * 
 * Configurable properties;
 * - sizeX, sizeY; Merged output SDR dimensions (input SDR dimensions are not restricted) 
 * - maxOnBits; Optional maximum number of on bits in the merged output (uses sub sampling) 
 * - distortion; Optional on bit distortion to the output SDR
 */
public class MergerConfig extends SDRProcessorConfig{
	public int				sizeX			= 768;
	public int				sizeY			= 48;
	public int				maxOnBits		= 256;
	public float			distortion		= 0.0F;
}
