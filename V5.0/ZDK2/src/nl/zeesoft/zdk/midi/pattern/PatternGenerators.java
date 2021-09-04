package nl.zeesoft.zdk.midi.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.MidiSys;

public class PatternGenerators {
	public List<PatternGenerator>	generators			= new ArrayList<PatternGenerator>();
	
	public PatternGenerators copy() {
		PatternGenerators r = new PatternGenerators();
		for (PatternGenerator generator: generators) {
			r.generators.add(generator.copy());
		}
		return r;
	}

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
	
	public InstrumentPattern generatePattern(String instrumentName, ChordPattern instrumentChordPattern, int start, int end) {
		InstrumentPattern r = new InstrumentPattern();
		r.name = instrumentName;
		if (instrumentChordPattern==null) {
			r.chordPattern = MidiSys.chordPattern;
		} else {
			r.chordPattern = instrumentChordPattern;
		}
		for (PatternGenerator pg: generators) {
			r.patterns.add(pg.generatePattern(start, end));
		}
		return r;
	}
}
