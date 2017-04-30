package nl.zeesoft.zmmt.syntesizer;

public abstract class VelocityConfiguration {
	private int 	baseVelocity		= 100;
	private	int		accentVelocity		= 110;
	
	private int 	layerBaseVelocity	= 100;
	private	int		layerAccentVelocity	= 110;
	
	public int getBaseVelocity() {
		return baseVelocity;
	}
	
	public void setBaseVelocity(int baseVelocity) {
		this.baseVelocity = baseVelocity;
	}
	
	public int getAccentVelocity() {
		return accentVelocity;
	}
	
	public void setAccentVelocity(int accentVelocity) {
		this.accentVelocity = accentVelocity;
	}

	public int getLayerBaseVelocity() {
		return layerBaseVelocity;
	}

	public void setLayerBaseVelocity(int layerBaseVelocity) {
		this.layerBaseVelocity = layerBaseVelocity;
	}

	public int getLayerAccentVelocity() {
		return layerAccentVelocity;
	}

	public void setLayerAccentVelocity(int layerAccentVelocity) {
		this.layerAccentVelocity = layerAccentVelocity;
	}
}
