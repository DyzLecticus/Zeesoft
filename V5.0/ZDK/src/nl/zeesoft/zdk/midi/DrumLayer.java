package nl.zeesoft.zdk.midi;

public class DrumLayer {
	public int		midiNoteNum		= 0;
	public int		velocity		= 100; 
	public float	stepPercentage	= 0.7F;
	
	public DrumLayer copy() {
		DrumLayer r = new DrumLayer();
		r.midiNoteNum = midiNoteNum;
		r.velocity = velocity;
		r.stepPercentage = stepPercentage;
		return r;
	}
}
