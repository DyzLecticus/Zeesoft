package nl.zeesoft.zdk.htm.impl;

public class SpatialPoolerConfig {
	protected boolean		initialized						= false;
	
	protected float			proximalConnections				= 0.85F;
	
	protected float			permanenceThreshold				= 0.1F;
	protected float			permanenceIncrement				= 0.05F;
	protected float			permanenceDecrement				= 0.008F;

	protected float			boostStrength					= 10;
	protected int			maxActivityLogSize				= 100;

	public void setProximalConnections(float potentialConnections) {
		if (!initialized) {
			this.proximalConnections = potentialConnections;
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

	public void setBoostStrength(float boostStrength) {
		if (!initialized) {
			this.boostStrength = boostStrength;
		}
	}

	public void setMaxActivityLogSize(int maxActivityLogSize) {
		if (!initialized) {
			this.maxActivityLogSize = maxActivityLogSize;
		}
	}
}
