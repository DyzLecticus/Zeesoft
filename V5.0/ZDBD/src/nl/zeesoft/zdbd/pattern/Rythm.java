package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.thread.RunCode;

public class Rythm {
	public static String	PATTERN				= "Pattern";
	public static String	STEP				= "Step";
	public static String	BEAT				= "Beat";
	
	public static String[]	ELEMENT_NAMES		= {PATTERN, STEP, BEAT};
	
	public float			beatsPerMinute		= 135;
	public int				beatsPerPattern 	= 4;
	public int				stepsPerBeat		= 4;
	
	public void copyFrom(Rythm rythm) {
		this.beatsPerMinute = rythm.beatsPerMinute;
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
			r.add(getSDRForPatternStep(pattern, s));
		}
		return r;
	}
	
	public SDR getSDRForPatternStep(int pattern, int step) {
		int[] value = new int[3];
		int beatStep = step % stepsPerBeat;
		int beat = (step - beatStep) / stepsPerBeat;
		value[0] = pattern;
		value[1] = beat;
		value[2] = beatStep;
		return EncoderFactory.contextEncoder.getEncodedValue(value);
	}
	
	public static int sizeX() {
		return getEmptyContextSDR().sizeX();
	}

	public static int sizeY() {
		return getEmptyContextSDR().sizeY();
	}
	
	public static SDR getEmptyContextSDR() {
		SDR r = new SDR(EncoderFactory.contextEncoder.getEncodeLength(),1);
		r.flatten();
		r.square();
		return r;
	}

	public RunCode getFromFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				fromFile(path);
				return true;
			}
		};
	}
	
	public void fromFile(String path) {
		Rythm rythm = (Rythm) PersistableCollection.fromFile(path);
		if (rythm!=null) {
			copyFrom(rythm);
		}
	}
	
	public RunCode getToFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				toFile(path);
				return true;
			}
		};
	}
	
	public void toFile(String path) {
		PersistableCollection.toFile(this, path);
	}
}
