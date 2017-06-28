package nl.zeesoft.zmmt.synthesizer;

public class EchoConfiguration {
	private String		instrument				= Instrument.LEAD;
	private int			layer					= 1;
	private int			steps					= 6;
	private int			velocityPercentage1		= 70;
	private int			velocityPercentage2		= 50;
	private int			velocityPercentage3		= 30;
	private int			reverb1					= 80;
	private int			reverb2					= 104;
	private int			reverb3					= 127;
	private int			pan1					= 0;
	private int			pan2					= 127;
	private int			pan3					= 64;
	
	public EchoConfiguration copy() {
		EchoConfiguration r = new EchoConfiguration();
		r.setInstrument(instrument);
		r.setLayer(layer);
		r.setSteps(steps);
		r.setVelocityPercentage1(velocityPercentage1);
		r.setVelocityPercentage2(velocityPercentage2);
		r.setVelocityPercentage3(velocityPercentage3);
		r.setReverb1(reverb1);
		r.setReverb2(reverb2);
		r.setReverb3(reverb3);
		r.setPan1(pan1);
		r.setPan2(pan2);
		r.setPan3(pan3);
		return r;
	}

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

	public int getPan1() {
		return pan1;
	}

	public void setPan1(int pan1) {
		this.pan1 = pan1;
	}

	public int getPan2() {
		return pan2;
	}

	public void setPan2(int pan2) {
		this.pan2 = pan2;
	}

	public int getPan3() {
		return pan3;
	}

	public void setPan3(int pan3) {
		this.pan3 = pan3;
	}
}
