package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.pattern.PatternGenerator;

public abstract class Instrument {
	public static String 			BASS			= "Bass";
	public static String 			STAB			= "Stab";
	public static String 			ARP				= "Arpeggiator";
	public static String 			DRUM			= "Drum";
	
	public String					name			= "";
	public List<PatternGenerator>	generators		= new ArrayList<PatternGenerator>();
	
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
}
