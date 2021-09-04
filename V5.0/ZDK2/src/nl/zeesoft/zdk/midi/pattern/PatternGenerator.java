package nl.zeesoft.zdk.midi.pattern;

import java.util.ArrayList;
import java.util.List;

public class PatternGenerator {
	public String						name			= "";
	public int							length			= 8;
	public List<PatternGeneratorStep>	steps			= new ArrayList<PatternGeneratorStep>();
	
	public PatternGenerator copy() {
		PatternGenerator r = new PatternGenerator();
		r.name = name;
		r.length = length;
		for (PatternGeneratorStep step: steps) {
			r.steps.add(step.copy());
		}
		return r;
	}

	public PatternGeneratorStep getStep(int step) {
		step = step % length;
		PatternGeneratorStep r = null;
		for (PatternGeneratorStep ps: steps) {
			if (ps.step==step) {
				r = ps;
				break;
			}
		}
		return r;
	}
	
	public void addStep(int step, boolean accent) {
		PatternGeneratorStep ps = new PatternGeneratorStep();
		ps.step = step;
		ps.accent = accent;
		steps.add(ps);
	}
	
	public Pattern generatePattern(int start, int end) {
		Pattern r = new Pattern();
		r.name = name;
		r.stepStart = start;
		r.stepEnd = end;
		int mod = end - start;
		for (int s = start; s < end; s++) {
			PatternGeneratorStep ps = getStep(s);
			if (ps!=null) {
				r.addStep(s % mod, ps.accent);
			}
		}
		return r;
	}
}
