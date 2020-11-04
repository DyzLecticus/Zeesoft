package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

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
		drums = new int[rythm.getStepsPerPattern()][6];
		for (int s = 0; s < rythm.getStepsPerPattern(); s++) {
			for (int d = 0; d < DRUM_NAMES.length; d++) {
				drums[s][d] = OFF;
			}
		}
	}
	
	public List<SDR> getSDRsForDrum(int drum) {
		List<SDR> r = new ArrayList<SDR>();
		int stepsPerPattern = rythm.getStepsPerPattern();
		for (int s = 0; s < stepsPerPattern; s++) {
			KeyValueSDR sdr = new KeyValueSDR(EncoderFactory.drumEncoder.getEncodedValue(drums[s][drum]));
			sdr.put(DRUM_NAMES[drum], drums[s][drum]);
			r.add(sdr);
		}
		return r;
	}
}
