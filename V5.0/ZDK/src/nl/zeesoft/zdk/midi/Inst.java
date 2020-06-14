package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

public class Inst {
	private static final int	BASE_OCTAVE		= 3;
	
	public static final String	INSTRUMENT		= "INSTRUMENT"; 
	public static final String	VOLUME			= "VOLUME"; 
	public static final String	ATTACK			= "ATTACK"; 
	public static final String	DECAY			= "DECAY"; 
	public static final String	RELEASE			= "RELEASE"; 

	public static final String	PAN				= "PAN"; 
	public static final String	PRESSURE		= "PRESSURE"; 
	public static final String	MODULATION		= "MODULATION"; 
	public static final String	CHORUS			= "CHORUS"; 

	public static final String	FILTER			= "FILTER"; 
	public static final String	RESONANCE		= "RESONANCE"; 
	public static final String	REVERB			= "REVERB"; 

	public static final String	VIB_RATE		= "VIB_RATE"; 
	public static final String	VIB_DEPTH		= "VIB_DEPTH"; 
	public static final String	VIB_DELAY		= "VIB_DELAY"; 

	public int					channel			= -1;
	
	public boolean				solo			= false;
	public boolean				mute			= false;

	public int					instrument		= 0;
	public int					volume			= 100;
	public int					attack			= 64;
	public int					decay			= 64;
	public int					release			= 64;
	
	public int					pan				= 64;
	public int					pressure		= 0;
	public int					modulation		= 0;
	public int					chorus			= 0;
	
	public int					filter			= 64;
	public int					resonance		= 64;
	public int					reverb			= 16;
	
	public int					vib_rate		= 64;
	public int					vib_depth		= 64;
	public int					vib_delay		= 64;
	
	public int					velocity		= 100;
	public int					baseOctave		= 3;
	public int					patchDelaySteps	= 0;
	
	public void setPropertyValue(String property, int value) {
		if (value < 0) {
			value = 0;
		} else if (value > 127) {
			value = 127;
		}
		if (property.equals(INSTRUMENT)) {
			instrument = value;
		} else if (property.equals(VOLUME)) {
			volume = value;
		} else if (property.equals(ATTACK)) {
			attack = value;
		} else if (property.equals(DECAY)) {
			decay = value;
		} else if (property.equals(RELEASE)) {
			release = value;
		} else if (property.equals(PAN)) {
			pan = value;
		} else if (property.equals(PRESSURE)) {
			pressure = value;
		} else if (property.equals(MODULATION)) {
			modulation = value;
		} else if (property.equals(CHORUS)) {
			chorus = value;
		} else if (property.equals(FILTER)) {
			filter = value;
		} else if (property.equals(RESONANCE)) {
			resonance = value;
		} else if (property.equals(REVERB)) {
			reverb = value;
		} else if (property.equals(VIB_RATE)) {
			vib_rate = value;
		} else if (property.equals(VIB_DEPTH)) {
			vib_depth = value;
		} else if (property.equals(VIB_DELAY)) {
			vib_delay = value;
		}
	}
	
	public int getPropertyValue(String property) {
		int r = -1;
		if (property.equals(INSTRUMENT)) {
			r = instrument;
		} else if (property.equals(VOLUME)) {
			r = volume;
		} else if (property.equals(ATTACK)) {
			r = attack;
		} else if (property.equals(DECAY)) {
			r = decay;
		} else if (property.equals(RELEASE)) {
			r = release;
		} else if (property.equals(PAN)) {
			r = pan;
		} else if (property.equals(PRESSURE)) {
			r = pressure;
		} else if (property.equals(MODULATION)) {
			r = modulation;
		} else if (property.equals(CHORUS)) {
			r = chorus;
		} else if (property.equals(FILTER)) {
			r = filter;
		} else if (property.equals(RESONANCE)) {
			r = resonance;
		} else if (property.equals(REVERB)) {
			r = reverb;
		} else if (property.equals(VIB_RATE)) {
			r = vib_rate;
		} else if (property.equals(VIB_DEPTH)) {
			r = vib_depth;
		} else if (property.equals(VIB_DELAY)) {
			r = vib_delay;
		}
		return r;
	}
	
	public Inst copy() {
		Inst r = new Inst();
		r.channel = channel;
		r.solo = solo;
		r.mute = mute;
		r.instrument = instrument;
		r.volume = volume;
		r.attack = attack;
		r.decay = decay;
		r.release = release;
		r.pan = pan;
		r.pressure = pressure;
		r.modulation = modulation;
		r.chorus = chorus;
		r.filter = filter;
		r.resonance = resonance;
		r.reverb = reverb;
		r.vib_rate = vib_rate;
		r.vib_depth = vib_depth;
		r.vib_delay = vib_delay;
		r.velocity = velocity;
		r.baseOctave = baseOctave;
		r.patchDelaySteps = patchDelaySteps;
		return r;
	}
	
	protected List<MidiNote> getNotes(String... notes) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		for (String note: notes) {
			MidiNote mn = new MidiNote();
			mn.fromString(note);
			if (mn.octave!=BASE_OCTAVE) {
				if (mn.octave>BASE_OCTAVE) {
					mn.octave = baseOctave + (mn.octave - BASE_OCTAVE);
				} else if (mn.octave<BASE_OCTAVE) {
					mn.octave = baseOctave - (BASE_OCTAVE - mn.octave);
				}
			}
			if (mn.velocity<0) {
				mn.velocity = velocity;
			}
			r.add(mn);
		}
		return r;
	}
}
