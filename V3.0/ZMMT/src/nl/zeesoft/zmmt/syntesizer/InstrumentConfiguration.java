package nl.zeesoft.zmmt.syntesizer;

public abstract class InstrumentConfiguration {
	private int 	baseVelocity	= 100;
	private	int		accentVelocity	= 110;
	
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
}
