package nl.zeesoft.zdbd.midi;

public class Arpeggiator {
	public int						minDuration		= 1;
	public int						maxDuration		= 2;
	public float					density			= 0.80F;
	public int						maxOctave		= 1;
	public int						maxInterval		= 12;
	public int						maxSteps		= 20;
	
	public Arpeggiator copy() {
		Arpeggiator r = new Arpeggiator();
		r.maxDuration = maxDuration;
		r.density = density;
		r.maxOctave = maxOctave;
		r.maxInterval = maxInterval;
		return r;
	}
}
