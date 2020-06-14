package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

public class Inst {
	private static final int	BASE_OCTAVE		= 3;
	
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
