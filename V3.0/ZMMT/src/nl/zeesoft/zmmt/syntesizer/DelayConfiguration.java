package nl.zeesoft.zmmt.syntesizer;

public class DelayConfiguration {
	private String		instrument			= "";
	private int			steps				= 6;
	private int			feedback			= 64;
	
	public String getInstrument() {
		return instrument;
	}
	
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	
	public int getSteps() {
		return steps;
	}
	
	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	public int getFeedback() {
		return feedback;
	}
	
	public void setFeedback(int feedback) {
		this.feedback = feedback;
	}
}
