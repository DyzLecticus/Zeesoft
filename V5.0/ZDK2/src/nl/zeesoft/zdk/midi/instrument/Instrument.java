package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.pattern.ChordPattern;
import nl.zeesoft.zdk.midi.pattern.InstrumentPattern;
import nl.zeesoft.zdk.midi.pattern.PatternGenerator;

public abstract class Instrument {
	public static String 			BASS				= "Bass";
	public static String 			STAB				= "Stab";
	public static String 			ARP					= "Arpeggiator";
	public static String 			DRUM				= "Drum";
	
	public String					name				= "";
	public List<PatternGenerator>	generators			= new ArrayList<PatternGenerator>();
	public ChordPattern				chordPattern		= null;
	
	public int						baseOctaveChannel1	= 3;
	public int						baseOctaveChannel2	= 3;
	
	public Instrument(String name) {
		this.name = name;
	}
	
	public abstract List<Integer> getChannels();
	
	public PatternGenerator addGenerator(String name) {
		PatternGenerator r = null;
		if (getGenerator(name)==null) {
			r = new PatternGenerator();
			r.name = name;
			generators.add(r);
		}
		return r;
	}
	
	public PatternGenerator getGenerator(String name) {
		PatternGenerator r = null;
		for (PatternGenerator pg: generators) {
			if (pg.name.equals(name)) {
				r = pg;
			}
		}
		return r;
	}
	
	public InstrumentPattern generatePattern() {
		return generatePattern(0, MidiSys.groove.getTotalSteps());
	}
	
	public InstrumentPattern generatePattern(int start, int end) {
		InstrumentPattern r = new InstrumentPattern();
		r.name = name;
		if (chordPattern==null) {
			r.chordPattern = MidiSys.chordPattern;
		} else {
			r.chordPattern = chordPattern;
		}
		for (PatternGenerator pg: generators) {
			r.patterns.add(pg.generatePattern(start, end));
		}
		return r;
	}
}
