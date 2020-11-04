package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.Str;

public class EncoderFactory {
	public static PatternEncoder	patternEncoder	= new PatternEncoder();
	public static StepEncoder		stepEncoder		= new StepEncoder();
	public static BeatEncoder		beatEncoder		= new BeatEncoder();
	public static DrumEncoder		drumEncoder		= new DrumEncoder();
	
	public static Str testEncoders() {
		Str r = patternEncoder.testNoOverlap();
		if (r.length()==0) {
			r = stepEncoder.testNoOverlap();
		}
		if (r.length()==0) {
			r = beatEncoder.testNoOverlap();
		}
		if (r.length()==0) {
			r = drumEncoder.testNoOverlap();
		}
		return r;
	}
}
