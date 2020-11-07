package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.encoders.DrumEncoder;
import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;

public class DrumPattern {
	public static final int			BASEBEAT		= 0;
	public static final int			SNARE			= 1;
	public static final int			CLOSED_HIHAT	= 2;
	public static final int			OPEN_HIHAT		= 3;
	public static final int			RIDE			= 4;
	public static final int			CYMBAL			= 5;
	
	public static final String[]	DRUM_NAMES		= {
		"Basebeat", "Snare", "ClosedHihat", "OpenHihat", "Ride", "Cymbal"
	};
	
	public static final int			OFF				= 0;
	public static final int			ON				= 1;
	public static final int			ACCENT			= 2;
	
	public Rythm					rythm			= new Rythm();
	
	public int 						num				= 0;
	public int[][]					drums			= null;
	
	public void initialize(Rythm rythm) {
		this.rythm.copyFrom(rythm);
		drums = new int[rythm.getStepsPerPattern()][DRUM_NAMES.length];
		for (int s = 0; s < rythm.getStepsPerPattern(); s++) {
			for (int d = 0; d < DRUM_NAMES.length; d++) {
				drums[s][d] = OFF;
			}
		}
	}
	
	public List<SDR> getSDRsForPattern() {
		List<SDR> r = new ArrayList<SDR>();
		int stepsPerPattern = rythm.getStepsPerPattern();
		for (int s = 0; s < stepsPerPattern; s++) {
			r.add(getSDRForPatternStep(s, drums[s], stepsPerPattern));
		}
		return r;
	}
	
	public static SDR getSDRForPatternStep(int step, int[] values, int stepsPerPattern) {
		SDR r = null;
		if (step < stepsPerPattern) {
			DrumEncoder enc = EncoderFactory.drumEncoder;
			KeyValueSDR kvSdr = new KeyValueSDR(enc.getEncodeSizeX() * DRUM_NAMES.length, enc.getEncodeSizeY());
			for (int d = 0; d < DRUM_NAMES.length; d++) {
				kvSdr.concat(enc.getEncodedValue(values[d]), d * enc.getEncodeLength());
				kvSdr.put(DRUM_NAMES[d], values[d]);
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
		int[] values = new int[DRUM_NAMES.length];
		for (int d = 0; d < DRUM_NAMES.length; d++) {
			values[d] = OFF;
		}
		SDR sdr = getSDRForPatternStep(0, values, 1);
		sdr.flatten();
		sdr.square();
		return sdr;
	}
}
