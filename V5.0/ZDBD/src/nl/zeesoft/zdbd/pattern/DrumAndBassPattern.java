package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.encoders.BassEncoder;
import nl.zeesoft.zdbd.neural.encoders.DrumEncoder;
import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;

public class DrumAndBassPattern {
	public static final int			BASEBEAT			= 0;
	public static final int			SNARE				= 1;
	public static final int			CLOSED_HIHAT		= 2;
	public static final int			OPEN_HIHAT			= 3;
	public static final int			RIDE				= 4;
	public static final int			CYMBAL				= 5;
	public static final int			BASS				= 6;
	
	public static final String[]	INSTRUMENT_NAMES	= {
		"Basebeat", "Snare", "ClosedHihat", "OpenHihat", "Ride", "Cymbal", "Bass"
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
			for (int d = 0; d < INSTRUMENT_NAMES.length; d++) {
				pattern[s][d] = OFF;
			}
		}
	}
	
	public void setDrumNote(int step, int drum, int note) {
		if (note>=OFF && note<=ACCENT) {
			pattern[step][drum] = note;
		}
	}
	
	public void setBassNote(int step, int duration, boolean accent) {
		if (duration>=1 && duration<=8) {
			int note = duration * 2;
			if (accent) {
				note++;
			}
			pattern[step][BASS] = note;
		}
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
			r.add(getSDRForPatternStep(s, pattern[s], stepsPerPattern));
		}
		return r;
	}
	
	public static SDR getSDRForPatternStep(int step, int[] values, int stepsPerPattern) {
		SDR r = null;
		if (step < stepsPerPattern) {
			DrumEncoder dEnc = EncoderFactory.drumEncoder;
			BassEncoder bEnc = EncoderFactory.bassEncoder;
			int sizeX = dEnc.getEncodeLength() * (INSTRUMENT_NAMES.length - 1) + bEnc.getEncodeLength();
			KeyValueSDR kvSdr = new KeyValueSDR(sizeX , 1);
			for (int i = 0; i < INSTRUMENT_NAMES.length; i++) {
				if (i==BASS) {
					kvSdr.concat(bEnc.getEncodedValue(values[i]), i * dEnc.getEncodeLength());
				} else {
					kvSdr.concat(dEnc.getEncodedValue(values[i]), i * dEnc.getEncodeLength());
				}
				kvSdr.put(INSTRUMENT_NAMES[i], values[i]);
			}
			r = kvSdr;
		}
		return r;
	}
	
	public static int sizeX() {
		return getExampleSDR().sizeX();
	}
	
	public static int sizeY() {
		return getExampleSDR().sizeY();
	}
	
	private static SDR getExampleSDR() {
		int[] values = new int[INSTRUMENT_NAMES.length];
		for (int i = 0; i < INSTRUMENT_NAMES.length; i++) {
			values[i] = OFF;
		}
		SDR sdr = getSDRForPatternStep(0, values, 1);
		sdr.flatten();
		sdr.square();
		return sdr;
	}
}
