package nl.zeesoft.zdk.midi.pattern;

public class ChordPatternStep {
	public int		step		= 0;
	public int		baseNote	= 3;
	public int[]	interval	= new int[3];
	
	public ChordPatternStep() {
		interval[0] = 2;
		interval[1] = 4;
		interval[2] = 7;
	}
}
