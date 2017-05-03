package nl.zeesoft.zmmt.syntesizer;

public abstract class VelocityConfiguration extends BaseConfiguration {
	private int 	layer1BaseVelocity		= 100;
	private	int		layer1AccentVelocity	= 110;
	private int 	layer2BaseVelocity		= 100;
	private	int		layer2AccentVelocity	= 110;

	public int getLayer1BaseVelocity() {
		return layer1BaseVelocity;
	}
	
	public void setLayer1BaseVelocity(int baseVelocity) {
		this.layer1BaseVelocity = baseVelocity;
	}
	
	public int getLayer1AccentVelocity() {
		return layer1AccentVelocity;
	}
	
	public void setLayer1AccentVelocity(int accentVelocity) {
		this.layer1AccentVelocity = accentVelocity;
	}

	public int getLayer2BaseVelocity() {
		return layer2BaseVelocity;
	}

	public void setLayer2BaseVelocity(int baseVelocity) {
		this.layer2BaseVelocity = baseVelocity;
	}

	public int getLayer2AccentVelocity() {
		return layer2AccentVelocity;
	}

	public void setLayer2AccentVelocity(int accentVelocity) {
		this.layer2AccentVelocity = accentVelocity;
	}
}
