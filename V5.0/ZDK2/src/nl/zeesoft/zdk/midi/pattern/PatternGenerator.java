package nl.zeesoft.zdk.midi.pattern;

import java.util.ArrayList;
import java.util.List;

public class PatternGenerator {
	public String						name			= "";
	public int							length			= 8;
	public List<PatternGeneratorStep>	steps			= new ArrayList<PatternGeneratorStep>();

	public int							midiNoteNum		= 36;
	public float						hold			= 0.75F;
	public int							velocity		= 100;
	public float						accentHold		= 0.75F;
	public int							accentVelocity	= 127;

	public PatternGeneratorStep getStep(int step) {
		step = step % length;
		PatternGeneratorStep r = null;
		for (PatternGeneratorStep ps: steps) {
			if (ps.step==step) {
				r = ps;
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
}
