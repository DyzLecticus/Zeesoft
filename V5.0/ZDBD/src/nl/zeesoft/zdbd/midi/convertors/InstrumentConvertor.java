package nl.zeesoft.zdbd.midi.convertors;

import java.util.List;

import nl.zeesoft.zdk.Instantiator;

public abstract class InstrumentConvertor {
	public String 	name	= "";
	
	public abstract List<MidiNote> getMidiNotesForPatternValue(int value);
	
	public InstrumentConvertor copy() {
		InstrumentConvertor r = (InstrumentConvertor) Instantiator.getNewClassInstance(this.getClass());
		r.copyFrom(this);
		return r;
	}
	
	protected void copyFrom(InstrumentConvertor conv) {
		this.name = conv.name;
	}
}
