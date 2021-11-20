package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.pattern.PatternGenerators;

public class PatternVariations {
	public List<PatternGenerators>		variations	= new ArrayList<PatternGenerators>();
	
	public PatternVariations() {
		variations.add(new PatternGenerators());
	}
}
