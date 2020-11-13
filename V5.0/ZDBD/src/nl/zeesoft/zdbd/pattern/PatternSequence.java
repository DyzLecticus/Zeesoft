package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

public class PatternSequence {
	public List<DrumAndBassPattern>		patterns	= new ArrayList<DrumAndBassPattern>();
	public int[]						sequence	= new int[4];
	
	public PatternSequence() {
		sequence[0] = 0;
		sequence[1] = 1;
		sequence[2] = 0;
		sequence[3] = 2;
	}

	public List<DrumAndBassPattern> getSequencedPatterns() {
		List<DrumAndBassPattern> r = new ArrayList<DrumAndBassPattern>();
		for (int i = 0; i < sequence.length; i++) {
			if (sequence[i] < patterns.size()) {
				r.add(patterns.get(sequence[i]));
			}
		}
		return r;
	}

}
