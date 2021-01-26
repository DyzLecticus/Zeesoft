package nl.zeesoft.zdbd.midi;

public class Arpeggiator {
	public int						maxDuration		= 2;
	public float					density			= 0.75F;
	public int						maxOctave		= 1;
	public int						maxInterval		= 12;
	
	public Arpeggiator copy() {
		Arpeggiator r = new Arpeggiator();
		r.maxDuration = maxDuration;
		r.density = density;
		r.maxOctave = maxOctave;
		r.maxInterval = maxInterval;
		return r;
	}
	
	
}
