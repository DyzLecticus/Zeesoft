package nl.zeesoft.zdk.neural.model;

import nl.zeesoft.zdk.matrix.Size;

public class CellConfig {
	public Size		size						= new Size(48,48,16);
	
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
	
	public void copyFrom(CellConfig other) {
		this.size = size.copy();
		
		this.maxSegmentsPerCell = other.maxSegmentsPerCell;
		this.maxSynapsesPerSegment = other.maxSynapsesPerSegment;
		
		this.initialPermanence = other.initialPermanence;
		this.permanenceThreshold = other.permanenceThreshold;
		this.permanenceIncrement = other.permanenceIncrement;
		this.permanenceDecrement = other.permanenceDecrement;
		
		this.segmentCreationSubsample = other.segmentCreationSubsample;
		
		this.distalSegmentDecrement = other.distalSegmentDecrement;
		this.apicalSegmentDecrement = other.apicalSegmentDecrement;
		
		this.distalPotentialRadius = other.distalPotentialRadius;
		this.apicalPotentialRadius = other.apicalPotentialRadius;

		this.activationThreshold = other.activationThreshold;
		this.matchingThreshold = other.matchingThreshold;
		this.maxNewSynapseCount = other.maxNewSynapseCount;
	}
}
