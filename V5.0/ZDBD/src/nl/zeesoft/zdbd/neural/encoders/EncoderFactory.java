package nl.zeesoft.zdbd.neural.encoders;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.AbstractScalarEncoder;

public class EncoderFactory {
	private static int				ON_BITS			= 2;
	public static ContextEncoder	contextEncoder	= new ContextEncoder(ON_BITS);
	public static DrumEncoder		drumEncoder		= new DrumEncoder(ON_BITS);
	public static BassEncoder		bassEncoder		= new BassEncoder(ON_BITS);
	
	public static Str testEncoders() {
		Str r = new Str();
		for (AbstractScalarEncoder encoder: getEncoders()) {
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
	
	public static List<AbstractScalarEncoder> getEncoders() {
		List<AbstractScalarEncoder> r = new ArrayList<AbstractScalarEncoder>();
		r.add(contextEncoder);
		r.add(drumEncoder);
		r.add(bassEncoder);
		return r;
	}
}
