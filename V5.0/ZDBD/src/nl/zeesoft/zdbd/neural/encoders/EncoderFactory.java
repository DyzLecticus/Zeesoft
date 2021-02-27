package nl.zeesoft.zdbd.neural.encoders;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.AbstractScalarEncoder;

public class EncoderFactory {
	private static int					ON_BITS				= 2;
	
	public static ContextEncoder		contextEncoder		= new ContextEncoder(ON_BITS);
	public static DrumEncoder			drumEncoder			= new DrumEncoder(ON_BITS);
	public static HihatEncoder			hihatEncoder		= new HihatEncoder(ON_BITS);
	public static CymbalEncoder			cymbalEncoder		= new CymbalEncoder(ON_BITS);
	public static PercussionEncoder		percussionEncoder	= new PercussionEncoder(ON_BITS);
	
	public static BassEncoder			bassEncoder			= new BassEncoder(ON_BITS);
	public static OctaveEncoder			octaveEncoder		= new OctaveEncoder(ON_BITS);
	public static NoteEncoder			noteEncoder			= new NoteEncoder(ON_BITS);
	public static StabEncoder			stabEncoder			= new StabEncoder(ON_BITS);
	public static ShiftEncoder			shiftEncoder		= new ShiftEncoder(ON_BITS);
	
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
		r.add(hihatEncoder);
		r.add(cymbalEncoder);
		r.add(percussionEncoder);
		r.add(bassEncoder);
		r.add(octaveEncoder);
		r.add(noteEncoder);
		r.add(stabEncoder);
		r.add(shiftEncoder);
		return r;
	}
}
