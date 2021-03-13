package nl.zeesoft.zdk.neural.model;

public class CellConfig {
	public int		maxSegmentsPerCell			= 256;
	public int		maxSynapsesPerSegment		= 256;
	
	public float	initialPermanence			= 0.21F;
	public float	permanenceThreshold			= 0.5F;
	public float	permanenceIncrement			= 0.1F;
	public float	permanenceDecrement			= 0.1F;
	
	public float	segmentCreationSubsample	= 0.9F;
	
	public float	distalSegmentDecrement		= 0.2F;
	public float	apicalSegmentDecrement		= 0.2F;

	public float	distalPotentialRadius		= 0;
	public float	apicalPotentialRadius		= 0;

	public int		activationThreshold			= 13;
	public int		matchingThreshold			= 10;
	public int		maxNewSynapseCount			= 20;
}
