package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.DrumAndBassPattern;

public class MonoConvertor extends InstrumentConvertor {
	public int		channel			= 0;
	public int		midiNote		= 32;
	public int		velocity		= 100;
	public int		accentVelocity	= 120;
	public float	hold			= 0.9F;
	
	@Override
	public List<MidiNote> getMidiNotesForPatternNote(int note) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		if (note!=DrumAndBassPattern.OFF) {
			MidiNote mn = new MidiNote();
			mn.channel = channel;
			mn.midiNote = midiNote;
			mn.hold = ((float)(DrumAndBassPattern.getDurationForNote(note) - 1)) + hold;
			boolean accent = DrumAndBassPattern.getAccentForNote(note);
			if (accent) {
				mn.velocity = accentVelocity;
			} else {
				mn.velocity = velocity;
			}
			r.add(mn);
		}
		return r;
	}
}
