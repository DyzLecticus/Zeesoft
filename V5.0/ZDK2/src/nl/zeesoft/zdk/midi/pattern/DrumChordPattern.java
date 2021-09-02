package nl.zeesoft.zdk.midi.pattern;

public class DrumChordPattern extends ChordPattern {
	public DrumChordPattern() {
		int[] interval1 = {15,9,10};
		addStep(0,11,interval1);
	}
}
