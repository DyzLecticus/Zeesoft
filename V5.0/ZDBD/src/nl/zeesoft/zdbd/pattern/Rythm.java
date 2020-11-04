package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdk.neural.SDR;

public class Rythm {
	public int beatsPerPattern 	= 4;
	public int stepsPerBeat		= 4;
	
	public void copyFrom(Rythm rythm) {
		this.beatsPerPattern = rythm.beatsPerPattern;
		this.stepsPerBeat = rythm.stepsPerBeat;
	}
	
	public int getStepsPerPattern() {
		return beatsPerPattern * stepsPerBeat;
	}
	
	public List<SDR> getSDRsForPattern(int pattern) {
		List<SDR> r = new ArrayList<SDR>();
		int stepsPerPattern = getStepsPerPattern();
		for (int s = 0; s < stepsPerPattern; s++) {
			r.add(getSDRForPatternStep(pattern, s, stepsPerPattern, stepsPerBeat));
		}
		return r;
	}
	
	public static SDR getSDRForPatternStep(int pattern, int step, int stepsPerPattern, int stepsPerBeat) {
		SDR r = null;
		if (step <  stepsPerPattern) {
			r = new SDR(EncoderFactory.patternEncoder.getEncodeLength() + EncoderFactory.stepEncoder.getEncodeLength() + EncoderFactory.beatEncoder.getEncodeLength(), 1);
			r.concat(EncoderFactory.patternEncoder.getEncodedValue(pattern), 0);
			r.concat(EncoderFactory.stepEncoder.getEncodedValue(step), EncoderFactory.patternEncoder.getEncodeLength());
			r.concat(EncoderFactory.beatEncoder.getEncodedValue(step % stepsPerBeat), EncoderFactory.patternEncoder.getEncodeLength() + EncoderFactory.stepEncoder.getEncodeLength());
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
		SDR sdr = getSDRForPatternStep(0, 0, 1, 1);
		sdr.flatten();
		sdr.square();
		return sdr;
	}
}
