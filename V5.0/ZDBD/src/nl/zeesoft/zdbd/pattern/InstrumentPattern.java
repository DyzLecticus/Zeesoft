package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.encoders.BassEncoder;
import nl.zeesoft.zdbd.neural.encoders.DrumEncoder;
import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdbd.neural.encoders.HihatEncoder;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;

public class InstrumentPattern {
	public static final int			KICK				= 0;
	public static final int			SNARE				= 1;
	public static final int			HIHAT				= 2;
	public static final int			RIDE				= 3;
	public static final int			CYMBAL				= 4;
	public static final int			BASS				= 5;
	
	public static final String[]	INSTRUMENT_NAMES	= {
		"Kick", "Snare", "Hihat", "Ride", "Cymbal", "Bass"
	};
	
	public static final int			OFF					= 0;
	public static final int			ON					= 1;
	public static final int			ACCENT				= 2;
	
	public static final int			BASS_ACCENT			= 1;
	
	public Rythm					rythm				= new Rythm();
	
	public int 						num					= 0;
	public int[][]					pattern				= null;
	
	public void initialize(Rythm rythm) {
		this.rythm.copyFrom(rythm);
		pattern = new int[rythm.getStepsPerPattern()][INSTRUMENT_NAMES.length];
		for (int s = 0; s < rythm.getStepsPerPattern(); s++) {
			for (int i = 0; i < INSTRUMENT_NAMES.length; i++) {
				pattern[s][i] = OFF;
			}
		}
	}
	
	public void setKick(int step, int note) {
		setDrumNote(step,KICK,note);
	}
	
	public void setSnare(int step, int note) {
		setDrumNote(step,SNARE,note);
	}
	
	public void setHihat(int step, boolean open, int note) {
		if (note>OFF && open) {
			note += 2;
		}
		pattern[step][HIHAT] = note;
	}
	
	public void setRide(int step, int note) {
		setDrumNote(step,RIDE,note);
	}
	
	public void setCymbal(int step, int note) {
		setDrumNote(step,CYMBAL,note);
	}

	public void setDrumNote(int step, int drum, int note) {
		if (note>=OFF && note<=ACCENT) {
			pattern[step][drum] = note;
		}
	}
	
	public void setBassNote(int step, int duration, boolean accent) {
		if (duration>=1 && duration<=8) {
			pattern[step][BASS] = getNoteForDuration(duration,accent);
		}
	}
	
	public static boolean isOpenHihat(int note) {
		boolean r = false;
		if (note>=3) {
			r = true;
		}
		return r;
	}
	
	public static int getNoteForDuration(int duration, boolean accent) {
		int r = 0;
		if (duration>=1 && duration<=8) {
			r = (duration * 2) - 1;
			if (accent) {
				r++;
			}
		}
		return r;
	}
	
	public static int getDurationForNote(int note) {
		int r = 0;
		if (note>0) {
			boolean odd = note % 2 == 1;
			if (odd) {
				note = note + 1;
			}
			r = note / 2;
		}
		return r;
	}
	
	public static boolean getAccentForNote(int note) {
		boolean r = false;
		if (note>0) {
			r = note % 2 == 0;
		}
		return r;
	}
	
	public List<SDR> getSDRsForPattern() {
		List<SDR> r = new ArrayList<SDR>();
		int stepsPerPattern = rythm.getStepsPerPattern();
		for (int s = 0; s < stepsPerPattern; s++) {
			r.add(getSDRForPatternStep(s));
		}
		return r;
	}
	
	public SDR getSDRForPatternStep(int step) {
		return getSDRForPatternStep(step, pattern[step]);
	}
	
	public static SDR getSDRForPatternStep(int step, int values[]) {
		SDR r = null;
		DrumEncoder dEnc = EncoderFactory.drumEncoder;
		HihatEncoder hEnc = EncoderFactory.hihatEncoder;
		BassEncoder bEnc = EncoderFactory.bassEncoder;
		int sizeX = (dEnc.getEncodeLength() * (INSTRUMENT_NAMES.length - 2)) + hEnc.getEncodeLength() + bEnc.getEncodeLength();
		KeyValueSDR kvSdr = new KeyValueSDR(sizeX , 1);
		int offset = 0;
		for (int i = 0; i < INSTRUMENT_NAMES.length; i++) {
			if (i==HIHAT) {
				kvSdr.concat(hEnc.getEncodedValue(values[i]), offset);
				offset += hEnc.getEncodeLength();
			} else if (i==BASS) {
				kvSdr.concat(bEnc.getEncodedValue(values[i]), offset);
				offset += bEnc.getEncodeLength();
			} else {
				kvSdr.concat(dEnc.getEncodedValue(values[i]), offset);
				offset += dEnc.getEncodeLength();
			}
			kvSdr.put(INSTRUMENT_NAMES[i], values[i]);
		}
		r = kvSdr;
		return r;
	}

	public static int sizeX() {
		return getEmptyPatternSDR().sizeX();
	}

	public static int sizeY() {
		return getEmptyPatternSDR().sizeY();
	}
	
	public static SDR getEmptyPatternSDR() {
		int l = EncoderFactory.drumEncoder.getEncodeLength() * (InstrumentPattern.INSTRUMENT_NAMES.length - 2);
		l += EncoderFactory.hihatEncoder.getEncodeLength();
		l += EncoderFactory.bassEncoder.getEncodeLength();
		SDR r = new SDR(l,1);
		r.flatten();
		r.square();
		return r;
	}
}
