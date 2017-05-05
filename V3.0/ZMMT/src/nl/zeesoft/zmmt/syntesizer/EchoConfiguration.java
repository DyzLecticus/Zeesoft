package nl.zeesoft.zmmt.syntesizer;

public class EchoConfiguration {
	private String		instrument				= Instrument.LEAD;
	private int			layer					= 1;
	private int			steps					= 6;
	private int			velocityPercentage1		= 60;
	private int			velocityPercentage2		= 40;
	private int			velocityPercentage3		= 20;
	private int			reverb1					= 103;
	private int			reverb2					= 115;
	private int			reverb3					= 127;
	
	public String getInstrument() {
		return instrument;
	}
	
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getVelocityPercentage1() {
		return velocityPercentage1;
	}

	public void setVelocityPercentage1(int velocityPercentage1) {
		this.velocityPercentage1 = velocityPercentage1;
	}

	public int getVelocityPercentage2() {
		return velocityPercentage2;
	}

	public void setVelocityPercentage2(int velocityPercentage2) {
		this.velocityPercentage2 = velocityPercentage2;
	}

	public int getVelocityPercentage3() {
		return velocityPercentage3;
	}

	public void setVelocityPercentage3(int velocityPercentage3) {
		this.velocityPercentage3 = velocityPercentage3;
	}

	public int getReverb1() {
		return reverb1;
	}

	public void setReverb1(int reverb1) {
		this.reverb1 = reverb1;
	}

	public int getReverb2() {
		return reverb2;
	}

	public void setReverb2(int reverb2) {
		this.reverb2 = reverb2;
	}

	public int getReverb3() {
		return reverb3;
	}

	public void setReverb3(int reverb3) {
		this.reverb3 = reverb3;
	}
}
