package nl.zeesoft.zdbd.midi.convertors;

import java.util.List;

public abstract class InstrumentConvertor {
	public String 	name	= "";
	
	public abstract List<MidiNote> getMidiNotesForPatternValue(int value);
}
