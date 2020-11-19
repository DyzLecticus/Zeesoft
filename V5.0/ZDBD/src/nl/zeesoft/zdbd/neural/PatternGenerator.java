package nl.zeesoft.zdbd.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdbd.pattern.DrumAndBassPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class PatternGenerator {
	public NetworkIO		prevIO				= null;
	
	public float			contextDistortion	= 0.0F;
	public float			patternDistortion	= 0.5F;
	public float			combinedDistortion	= 0.0F;
	
	public boolean			getRandomChunks		= true;
	public List<Integer>	skipInstruments		= new ArrayList<Integer>();

	public PatternSequence generatePatternSequence(Network network, PatternSequence sequence) {
		PatternSequence r = new PatternSequence();
		for (DrumAndBassPattern pattern: sequence.patterns) {
			r.patterns.add(generatePattern(network,sequence,null,pattern.num));
		}
		for (int p = 0; p < r.sequence.length; p++) {
			r.sequence[p] = sequence.sequence[p];
		}
		return r;
	}
	
	public DrumAndBassPattern generatePattern(Network network, PatternSequence sequence, int baseSequencePattern) {
		return generatePattern(network,sequence,null,baseSequencePattern);
	}
	
	public DrumAndBassPattern generatePattern(Network network, PatternSequence sequence, Rythm rythm) {
		return generatePattern(network,sequence,rythm,-1);
	}

	protected DrumAndBassPattern generatePattern(Network network, PatternSequence sequence, Rythm rythm, int baseSequencePattern) {
		List<SDR> rythmSDRs = new ArrayList<SDR>();
		
		DrumAndBassPattern basePattern = null;
		if (baseSequencePattern>=0) {
			basePattern = sequence.patterns.get(baseSequencePattern);
		}
		if (rythm==null && basePattern!=null) {
			rythm = basePattern.rythm;
		}
		List<Integer> chunkSizes = new ArrayList<Integer>();
		chunkSizes.add(rythm.stepsPerBeat);
		if (rythm.stepsPerBeat<=5) {
			chunkSizes.add(rythm.stepsPerBeat - 1);
		} else {
			chunkSizes.add(rythm.stepsPerBeat - 2);
		}
		
		int stepsPerPattern = rythm.getStepsPerPattern();
		int len = chunkSizes.get(Rand.getRandomInt(0, chunkSizes.size() - 1));
		for (int s = 0; s < stepsPerPattern; s++) {
			List<SDR> chunk = getRandomBeatChunk(sequence);
			for (int c = 0; c < len; c++) {
				rythmSDRs.add(chunk.get(c));
				if (rythmSDRs.size()>=stepsPerPattern) {
					s = stepsPerPattern;
					break;
				}
				if (c>0) {
					s++;
				}
			}
			len = chunkSizes.get(Rand.getRandomInt(0, chunkSizes.size() - 1));
		}
		
		return generatePattern(network, rythm, rythmSDRs, basePattern);
	}

	protected List<SDR> getRandomBeatChunk(PatternSequence sequence) {
		List<SDR> r = new ArrayList<SDR>();
		int num = Rand.getRandomInt(0, sequence.patterns.size() - 1);
		DrumAndBassPattern pattern = sequence.patterns.get(num);
		List<SDR> sdrs = pattern.rythm.getSDRsForPattern(pattern.num);
		int startBeat = 0;
		if (getRandomChunks) {
			int lastBeat = ((pattern.rythm.getStepsPerPattern() - pattern.rythm.stepsPerBeat) - 1) / 2;
			startBeat = Rand.getRandomInt(0, lastBeat);
			startBeat = startBeat * 2;
		} else {
			startBeat = Rand.getRandomInt(0, pattern.rythm.beatsPerPattern - 1);
			startBeat = startBeat * pattern.rythm.beatsPerPattern;
		}
		for (int s = startBeat; s < startBeat + pattern.rythm.stepsPerBeat; s++) {
			r.add(sdrs.get(s));
		}
		return r;
	}
	
	protected DrumAndBassPattern generatePattern(Network network, Rythm rythm, List<SDR> rythmSDRs, DrumAndBassPattern basePattern) {
		DrumAndBassPattern r = new DrumAndBassPattern();
		r.initialize(rythm);
		
		if (prevIO!=null) {
			network.setProcessorProperty(NetworkConfigFactory.CONTEXT_INPUT + "Merger", "distortion", contextDistortion);
			network.setProcessorProperty(NetworkConfigFactory.PATTERN_INPUT + "Merger", "distortion", patternDistortion);
			network.setProcessorProperty("Merger", "distortion", combinedDistortion);
			
			int stepsPerPattern = rythm.getStepsPerPattern();
	
			// Generate pattern
			for (int s = 0 ; s < stepsPerPattern; s++) {
				int values[] = getPredictedValuesFromPreviousIO(prevIO);
				for (int i = 0; i < values.length; i++) {
					if (!skipInstruments.contains(i)) {
						r.pattern[s][i] = values[i];
					} else if (basePattern!=null && basePattern.pattern.length>s) {
						r.pattern[s][i] = basePattern.pattern[s][i];
					}
				}
				SDR	contextSDR = rythmSDRs.get(s);
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
		} else {
			Logger.err(this, new Str("Previous network IO has not been specified"));
		}
		return r;
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
