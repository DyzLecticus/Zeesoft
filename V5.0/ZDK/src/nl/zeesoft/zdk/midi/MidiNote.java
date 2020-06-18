package nl.zeesoft.zdk.midi;

public class MidiNote extends NoteObject {
	public int						channel			= 0;
	public int						delaySteps		= 0;
	public float					stepPercentage	= 0;
	
	@Override
	public MidiNote copy() {
		MidiNote r = (MidiNote) super.copy();
		r.channel = channel;
		r.delaySteps = delaySteps;
		r.stepPercentage = stepPercentage;
		return r;
	}
}
