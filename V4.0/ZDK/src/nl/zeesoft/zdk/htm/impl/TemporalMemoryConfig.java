package nl.zeesoft.zdk.htm.impl;

import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.functions.ZActivator;

public class TemporalMemoryConfig {
	protected boolean		initialized						= false;
	
	protected int			localDistalConnectionRadius		= 25;
	protected int			maxDistalDendritesPerCell		= 256;
	protected int			maxDistalSynapsesPerDendrite	= 256;
	protected ZActivator	distalActivator					= StaticFunctions.SIGMOID;
	protected float			distalActivityThreshold			= 0.2F;
	
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

	public void setDistalActivator(ZActivator distalActivator) {
		if (!initialized) {
			this.distalActivator = distalActivator;
		}
	}

	public void setDistalActivityThreshold(float distalActivityThreshold) {
		if (!initialized) {
			this.distalActivityThreshold = distalActivityThreshold;
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
