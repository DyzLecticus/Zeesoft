package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.inst.PatternInstrument;

public class MonoConvertor extends InstrumentConvertor {
	public int		channel			= 0;
	public int		midiNote		= 32;
	public int		velocity		= 100;
	public int		accentVelocity	= 120;
	public float	hold			= 0.9F;
	
	@Override
	public List<MidiNote> getMidiNotesForPatternValue(int value) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		if (value!=PatternInstrument.OFF) {
			MidiNote mn = new MidiNote();
			mn.channel = channel;
			mn.midiNote = midiNote;
			mn.hold = ((float)(InstrumentPattern.getDurationForValue(value) - 1)) + hold;
			if (InstrumentPattern.isAccent(value)) {
				mn.velocity = accentVelocity;
			} else {
				mn.velocity = velocity;
			}
			r.add(mn);
		}
		return r;
	}
}
