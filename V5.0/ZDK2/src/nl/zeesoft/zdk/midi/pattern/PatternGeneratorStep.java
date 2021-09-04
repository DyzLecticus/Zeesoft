package nl.zeesoft.zdk.midi.pattern;

public class PatternGeneratorStep {
	public int		step		= 0;
	public boolean	accent		= false;
	
	public PatternGeneratorStep copy() {
		PatternGeneratorStep r = new PatternGeneratorStep();
		r.step = step;
		r.accent = accent;
		return r;
	}
}
