package nl.zeesoft.zmmt.synthesizer;

public class MidiNoteDelayed extends MidiNote {
	public int		delaySteps		= 0;
	public long		playDateTime	= 0;

	public MidiNote getNewMidiNote() {
		MidiNote r = new MidiNote();
		r.instrument = instrument;
		r.note = note;
		r.channel = channel;
		r.midiNote = midiNote;
		r.velocity = velocity;
		return r;
	}
}
