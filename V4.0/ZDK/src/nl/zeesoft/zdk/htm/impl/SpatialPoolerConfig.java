package nl.zeesoft.zdk.htm.impl;

public class SpatialPoolerConfig {
	protected boolean		initialized						= false;
	
	protected float			proximalConnections				= 0.85F;
	
	protected float			permanenceThreshold				= 0.1F;
	protected float			permanenceIncrement				= 0.05F;
	protected float			permanenceDecrement				= 0.008F;

	public void setProximalConnections(float potentialConnections) {
		if (!initialized) {
			this.proximalConnections = potentialConnections;
		}
	}
}
