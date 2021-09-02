package nl.zeesoft.zdk.midi.pattern;

public class DefaultChordPattern extends ChordPattern {
	public DefaultChordPattern() {
		int[] interval1 = {2,4,7};
		addStep(0,3,interval1);
		int[] interval2 = {2,5,7};
		addStep(32,3,interval2);
	}
}
