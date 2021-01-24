package nl.zeesoft.zdbd.pattern;

public class SequenceChord {
	public int		step		= 0;
	
	public int		baseNote	= 3;
	public int[]	interval	= new int[3];
	
	public SequenceChord() {
		interval[0] = 2;
		interval[1] = 4;
		interval[2] = 7;
	}
	
	public SequenceChord copy() {
		SequenceChord r = new SequenceChord();
		r.step = step;
		r.baseNote = baseNote;
		for (int i = 0; i < interval.length; i++) {
			r.interval[i] = interval[i];
		}
		return r;
	}
}
