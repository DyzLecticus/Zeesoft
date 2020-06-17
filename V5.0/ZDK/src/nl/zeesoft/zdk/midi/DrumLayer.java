package nl.zeesoft.zdk.midi;

public class DrumLayer {
	public int		midiNoteNum		= 36;
	public int		velocity		= 127; 
	public float	stepPercentage	= 0.7F;
	
	public DrumLayer copy() {
		DrumLayer r = new DrumLayer();
		r.midiNoteNum = midiNoteNum;
		r.velocity = velocity;
		r.stepPercentage = stepPercentage;
		return r;
	}
	
	public int getOctave() {
		return (midiNoteNum - (midiNoteNum % 12)) / 12; 
	}
	
	public int getOctaveNote() {
		return midiNoteNum - (getOctave() * 12); 
	}
}
