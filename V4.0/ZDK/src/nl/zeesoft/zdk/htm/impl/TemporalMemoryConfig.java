package nl.zeesoft.zdk.htm.impl;

public class TemporalMemoryConfig {
	protected boolean		initialized						= false;
	
	protected int			localDistalConnectionRadius		= 25;
	protected int			maxDistalDendritesPerCell		= 256;
	protected int			maxDistalSynapsesPerDendrite	= 256;
	
	protected float			permanenceThreshold				= 0.1F;
	protected float			permanenceIncrement				= 0.05F;
	protected float			permanenceDecrement				= 0.008F;

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
