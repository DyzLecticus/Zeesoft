package nl.zeesoft.zdk.htm.impl;

public class TemporalMemoryConfig {
	protected boolean		initialized						= false;
	
	protected int			localDistalConnectionRadius		= 64;
	protected int			maxDistalDendritesPerCell		= 256;
	protected int			maxDistalSynapsesPerDendrite	= 256;
	
	protected int			minActiveSynapses				= 2;
	protected int			minMatchingSynapses				= 1;
	
	protected float			permanenceThreshold				= 0.5F;
	protected float			permanenceIncrement				= 0.1F;
	protected float			permanenceDecrement				= 0.1F;

	public void setLocalDistalConnectionRadius(int localDistalConnectionRadius) {
		if (!initialized) {
			this.localDistalConnectionRadius = localDistalConnectionRadius;
		}
	}

	public void setMaxDistalDendritesPerCell(int maxDistalDendritesPerCell) {
		if (!initialized) {
			this.maxDistalDendritesPerCell = maxDistalDendritesPerCell;
		}
	}

	public void setMaxDistalSynapsesPerDendrite(int maxDistalSynapsesPerDendrite) {
		if (!initialized) {
			this.maxDistalSynapsesPerDendrite = maxDistalSynapsesPerDendrite;
		}
	}

	public void setMinActiveSynapses(int minActiveSynapses) {
		if (!initialized) {
			this.minActiveSynapses = minActiveSynapses;
		}
	}

	public void setMinMatchingSynapses(int minMatchingSynapses) {
		if (!initialized) {
			this.minMatchingSynapses = minMatchingSynapses;
		}
	}

	public void setPermanenceThreshold(float permanenceThreshold) {
		if (!initialized) {
			this.permanenceThreshold = permanenceThreshold;
		}
	}

	public void setPermanenceIncrement(float permanenceIncrement) {
		if (!initialized) {
			this.permanenceIncrement = permanenceIncrement;
		}
	}

	public void setPermanenceDecrement(float permanenceDecrement) {
		if (!initialized) {
			this.permanenceDecrement = permanenceDecrement;
		}
	}
}
