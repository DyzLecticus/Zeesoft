package nl.zeesoft.zdk.neural;

public class TemporalMemoryConfig extends SDRProcessorConfig{
	public int		sizeX						= 48;
	public int		sizeY						= 48;
	public int		sizeZ						= 16;
	
	public int		maxSegmentsPerCell			= 256;
	public int		maxSynapsesPerSegment		= 256;
	
	public float	initialPermanence			= 0.21F;
	public float	permanenceThreshold			= 0.5F;
	public float	permanenceIncrement			= 0.1F;
	public float	permanenceDecrement			= 0.1F;
	
	public float	distalSegmentDecrement		= 0.2F;
	public float	apicalSegmentDecrement		= 0.2F;

	public int		distalPotentialRadius		= 0;
	// Assumes apical input XY dimensions match model XY dimensions
	public int		apicalPotentialRadius		= 0;

	public int		activationThreshold			= 13;
	public int		matchingThreshold			= 10;
	public int		maxNewSynapseCount			= 20;
}
