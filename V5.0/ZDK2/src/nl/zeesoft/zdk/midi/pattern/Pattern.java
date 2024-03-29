package nl.zeesoft.zdk.midi.pattern;

import java.util.ArrayList;
import java.util.List;

public class Pattern {
	public String				elementName	= "";
	public int					stepStart	= 0;
	public int					stepEnd		= 0;
	public List<PatternStep>	steps		= new ArrayList<PatternStep>();
	
	public PatternStep getStep(int step) {
		PatternStep r = null;
		for (PatternStep ps: steps) {
			if (ps.step==step) {
				r = ps;
				break;
			}
		}
		return r;
	}
	
	public PatternStep getNextStep(int step) {
		PatternStep r = null;
		for (PatternStep ps: steps) {
			if (ps.step>step && (r==null || r.step>ps.step)) {
				r = ps;
			}
		}
		return r;
	}
	
	public void addStep(int step, boolean accent) {
		PatternStep ps = new PatternStep();
		ps.step = step;
		ps.accent = accent;
		steps.add(ps);
	}
}
