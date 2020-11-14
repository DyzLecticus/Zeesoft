package nl.zeesoft.zdbd.neural;

import java.util.SortedMap;

import nl.zeesoft.zdbd.pattern.DrumAndBassPattern;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class PatternGenerator {
	public float		contextDistortion	= 0.0F;
	public float		patternDistortion	= 0.0F;
	public float		combinedDistortion	= 0.0F;
	public NetworkIO	prevIO				= null;
	
	public DrumAndBassPattern generatePattern(Network network, Rythm rythm, int pattern) {
		DrumAndBassPattern r = new DrumAndBassPattern();
		r.initialize(rythm);
		
		network.setProcessorProperty(NetworkConfigFactory.CONTEXT_INPUT + "Merger", "distortion", contextDistortion);
		network.setProcessorProperty(NetworkConfigFactory.PATTERN_INPUT + "Merger", "distortion", patternDistortion);
		network.setProcessorProperty("Merger", "distortion", combinedDistortion);
		
		int stepsPerPattern = rythm.getStepsPerPattern();

		// Generate pattern
		for (int s = 0 ; s < stepsPerPattern; s++) {
			int values[] = getPredictedValuesFromPreviousIO(prevIO);
			for (int i = 0; i < values.length; i++) {
				r.pattern[s][i] = values[i];
			}
			SDR	contextSDR	= Rythm.getSDRForPatternStep(pattern, s, stepsPerPattern, rythm.stepsPerBeat);
			SDR patternSDR = DrumAndBassPattern.getSDRForPatternStep(s, values, stepsPerPattern);
			NetworkIO networkIO = new NetworkIO();
			networkIO.setValue(NetworkConfigFactory.CONTEXT_INPUT, contextSDR);
			networkIO.setValue(NetworkConfigFactory.PATTERN_INPUT, patternSDR);
			network.processIO(networkIO);
			if (networkIO.hasErrors()) {
				Logger.err(this, networkIO.getErrors().get(0));
				break;
			}
			prevIO = networkIO;
		}
		
		
		network.setProcessorProperty(NetworkConfigFactory.CONTEXT_INPUT + "Merger", "distortion", 0F);
		network.setProcessorProperty(NetworkConfigFactory.PATTERN_INPUT + "Merger", "distortion", 0F);
		network.setProcessorProperty("Merger", "distortion", 0F);
		return r;
	}
	
	public void generatePatternStep(Network network, DrumAndBassPattern pattern, NetworkIO prevIO) {
		
	}
	
	protected int[] getPredictedValuesFromPreviousIO(NetworkIO prevIO) {
		int r[] = new int[DrumAndBassPattern.INSTRUMENT_NAMES.length];
		SortedMap<String,Object> predictedValues = prevIO.getClassificationValues(1);
		for (int i = 0; i < DrumAndBassPattern.INSTRUMENT_NAMES.length; i++) {
			Object value = predictedValues.get(DrumAndBassPattern.INSTRUMENT_NAMES[i] + "Classifier");
			if (value==null) {
				value = DrumAndBassPattern.OFF;
			}
			r[i] = (int) value;
		}
		return r;
	}
}
