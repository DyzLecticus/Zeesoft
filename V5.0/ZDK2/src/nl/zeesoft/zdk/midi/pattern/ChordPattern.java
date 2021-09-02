package nl.zeesoft.zdk.midi.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.MidiSys;

public class ChordPattern {
	public List<ChordPatternStep>	steps	= new ArrayList<ChordPatternStep>();
	
	public ChordPatternStep getStep(int step) {
		step = step % MidiSys.groove.getTotalSteps();
		ChordPatternStep r = null;
		for (int s = step; s >= 0; s--) {
			for (ChordPatternStep ps: steps) {
				if (ps.step==s) {
					r = ps;
					break;
				}
			}
			if (r!=null) {
				break;
			}
		}
		return r;
	}
	
	public void addStep(int step, int baseNote, int[] interval) {
		ChordPatternStep ps = new ChordPatternStep();
		ps.step = step;
		ps.baseNote = baseNote;
		ps.interval = interval;
		steps.add(ps);
	}
}
