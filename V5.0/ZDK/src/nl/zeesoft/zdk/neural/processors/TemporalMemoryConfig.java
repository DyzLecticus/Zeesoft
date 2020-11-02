package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Str;

/**
 * This class can be used to configure a TemporalMemory before initialization.
 */
public class TemporalMemoryConfig extends SDRProcessorConfig{
	public static String 	DOCUMENTATION 		=
		"Please note: The current implementation does not generate any initial segments/synapses.  \n" + 
		"  \n" + 
		"Configurable properties;  \n" + 
		" * *sizeX*, *sizeY*, *sizeZ*; Cell grid dimensions (sizeX, sizeY specify input SDR dimensions).  \n" + 
		" * *maxSegmentsPerCell*; Maximum number of segments per cell.  \n" +
		" * *maxSynapsesPerSegment*; Maximum number of synapses per segment.  \n" +
		" * *initialPermanence*, *permanenceThreshold*, *permanenceIncrement*, *permanenceDecrement*; Distal and apical synapse adaptation control.  \n" +
		" * *distalSegmentDecrement*, *apicalSegmentDecrement*; Optional segment decrement for distal/apical segments.  \n" + 
		" * *distalPotentialRadius*; Optional potential radius for distal segments.  \n" + 
		" * *apicalPotentialRadius*; Optional potential radius for apical segments (assumes apical input XY dimensions match model XY dimensions).  \n" + 
		" * *activationThreshold*; Number of active synapses on a segment for it to be considered active.  \n" + 
		" * *matchingThreshold*; Number of potential synapses on a segment for it to be considered matching.  \n" + 
		" * *maxNewSynapseCount*; Maximum number of synapses to create when creating/adapting segments.  \n" + 
		"";

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
	public int		apicalPotentialRadius		= 0;

	public int		activationThreshold			= 13;
	public int		matchingThreshold			= 10;
	public int		maxNewSynapseCount			= 20;

	@Override
	public Str getDescription() {
		Str r = super.getDescription();
		
		r.sb().append(": ");
		r.sb().append(sizeX);
		r.sb().append("*");
		r.sb().append(sizeY);
		r.sb().append("*");
		r.sb().append(sizeZ);

		r.sb().append("\n");
		r.sb().append("-> 0 = Active cells: ");
		r.sb().append((sizeX * sizeZ));
		r.sb().append("*");
		r.sb().append(sizeY);

		r.sb().append("\n");
		r.sb().append("-> 1 = Bursting columns: ");
		r.sb().append(sizeX);
		r.sb().append("*");
		r.sb().append(sizeY);

		r.sb().append("\n");
		r.sb().append("-> 2 = Predictive cells: ");
		r.sb().append((sizeX * sizeZ));
		r.sb().append("*");
		r.sb().append(sizeY);
		
		r.sb().append("\n");
		r.sb().append("-> 3 = Winner cells: ");
		r.sb().append((sizeX * sizeZ));
		r.sb().append("*");
		r.sb().append(sizeY);
		return r;
	}
}
