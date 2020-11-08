package nl.zeesoft.zdbd.neural.encoders;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class EncoderFactory {
	public static PatternEncoder	patternEncoder	= new PatternEncoder();
	public static StepEncoder		stepEncoder		= new StepEncoder();
	public static BeatEncoder		beatEncoder		= new BeatEncoder();
	public static DrumEncoder		drumEncoder		= new DrumEncoder();
	public static BassEncoder		bassEncoder		= new BassEncoder();
	
	public static Str testEncoders() {
		Str r = new Str();
		for (BasicScalarEncoder encoder: getEncoders()) {
			r = encoder.testNoOverlap();
			if (r.length()==0) {
				r = encoder.testOnBits();
			}
			if (r.length()>0) {
				break;
			}
		}
		return r;
	}
	public static List<BasicScalarEncoder> getEncoders() {
		List<BasicScalarEncoder> r = new ArrayList<BasicScalarEncoder>();
		r.add(patternEncoder);
		r.add(stepEncoder);
		r.add(beatEncoder);
		r.add(drumEncoder);
		r.add(bassEncoder);
		return r;
	}

}
