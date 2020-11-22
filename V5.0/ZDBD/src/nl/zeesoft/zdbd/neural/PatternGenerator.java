package nl.zeesoft.zdbd.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;
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
	
	// Network merger dirsortion controls
	public float			contextDistortion	= 0.0F; // 0 - 1
	public float			patternDistortion	= 0.1F; // 0 - 1
	public float			combinedDistortion	= 0.0F; // 0 - 1
	
	// Randomized rythm generation controls
	public int				smallerChunk		= 1;
	public int				largerChunk			= 2;
	public boolean			randomChunkOffset	= true;
	
	// Mix controls
	public float			mixStart			= 0.5F; // 0 - 1
	public float			mixMid				= 1.0F; // 0 - 1
	public float			mixEnd				= 0.0F; // 0 - 1
	public float			maintainBeatFactor	= 1.0F; // <= 1 off, > 1 on
	public List<Integer>	skipInstruments		= new ArrayList<Integer>();

	public PatternSequence generatePatternSequence(Network network, PatternSequence sequence) {
		PatternSequence r = new PatternSequence();
		for (InstrumentPattern pattern: sequence.patterns) {
			r.patterns.add(generatePattern(network,sequence,null,pattern.num));
		}
		for (int p = 0; p < r.sequence.length; p++) {
			r.sequence[p] = sequence.sequence[p];
		}
		return r;
	}
	
	public InstrumentPattern generatePattern(Network network, PatternSequence sequence, int baseSequencePattern) {
		return generatePattern(network,sequence,null,baseSequencePattern);
	}
	
	public InstrumentPattern generatePattern(Network network, PatternSequence sequence, Rythm rythm) {
		return generatePattern(network,sequence,rythm,-1);
	}

	protected InstrumentPattern generatePattern(Network network, PatternSequence sequence, Rythm rythm, int baseSequencePattern) {
		InstrumentPattern basePattern = null;
		if (baseSequencePattern>=0) {
			basePattern = sequence.patterns.get(baseSequencePattern);
		}
		if (rythm==null && basePattern!=null) {
			rythm = basePattern.rythm;
		}
		List<SDR> rythmSDRs = generateRythmSDRs(sequence,rythm);
		return generatePattern(network, rythm, rythmSDRs, basePattern);
	}
	
	protected List<SDR> generateRythmSDRs(PatternSequence sequence, Rythm rythm) {
		List<SDR> r = new ArrayList<SDR>();
		
		List<Integer> chunkSizes = new ArrayList<Integer>();
		chunkSizes.add(rythm.stepsPerBeat);
		if (smallerChunk>0) {
			int size = rythm.stepsPerBeat - smallerChunk;
			if (size<1) {
				size = 1;
			}
			if (size < rythm.stepsPerBeat) {
				chunkSizes.add(rythm.stepsPerBeat - smallerChunk);
			}
		}
		if (largerChunk>0) {
			chunkSizes.add(rythm.stepsPerBeat + largerChunk);
		}
		
		int stepsPerPattern = rythm.getStepsPerPattern();
		int size = chunkSizes.get(Rand.getRandomInt(0, chunkSizes.size() - 1));
		for (int s = 0; s < stepsPerPattern; s++) {
			List<SDR> chunk = getRandomBeatChunk(sequence, size);
			for (int c = 0; c < size; c++) {
				r.add(chunk.get(c));
				if (r.size()>=stepsPerPattern) {
					s = stepsPerPattern;
					break;
				}
				if (c>0) {
					s++;
				}
			}
			size = chunkSizes.get(Rand.getRandomInt(0, chunkSizes.size() - 1));
		}
		
		return r;
	}

	protected List<SDR> getRandomBeatChunk(PatternSequence sequence, int size) {
		List<SDR> r = new ArrayList<SDR>();
		int num = Rand.getRandomInt(0, sequence.patterns.size() - 1);
		InstrumentPattern pattern = sequence.patterns.get(num);
		List<SDR> sdrs = pattern.rythm.getSDRsForPattern(pattern.num);
		int startBeat = Rand.getRandomInt(0, pattern.rythm.beatsPerPattern - 1);
		int step = startBeat * pattern.rythm.stepsPerBeat;
		if (randomChunkOffset) {
			step += Rand.getRandomInt(0, pattern.rythm.stepsPerBeat);
		}
		for (int s = 0; s < size; s++) {
			if (step>=sdrs.size()) {
				step = 0;
			}
			r.add(sdrs.get(step));
			step++;
		}
		return r;
	}
	
	protected InstrumentPattern generatePattern(Network network, Rythm rythm, List<SDR> rythmSDRs, InstrumentPattern basePattern) {
		InstrumentPattern r = new InstrumentPattern();
		r.initialize(rythm);
		if (basePattern!=null) {
			r.num = basePattern.num; 
		}
		
		if (prevIO!=null) {
			NetworkIO workingIO = prevIO;
			network.setProcessorProperty(NetworkConfigFactory.CONTEXT_INPUT + "Merger", "distortion", contextDistortion);
			network.setProcessorProperty(NetworkConfigFactory.PATTERN_INPUT + "Merger", "distortion", patternDistortion);
			network.setProcessorProperty("Merger", "distortion", combinedDistortion);
			
			List<SDR> originalRythmSDRs = rythm.getSDRsForPattern(r.num);
			
			// Generate pattern
			int stepsPerPattern = rythm.getStepsPerPattern();
			float mix = mixStart;
			float mixIncrementPerStep = (mixMid - mixStart) / (stepsPerPattern / 2);
			float bound = mixMid;
			for (int s = 0 ; s < stepsPerPattern; s++) {
				int values[] = getPredictedValuesFromPreviousIO(workingIO);
				for (int i = 0; i < values.length; i++) {
					if (!skipInstruments.contains(i)) {
						r.pattern[s][i] = values[i];
					} else if (basePattern!=null && basePattern.pattern.length>s) {
						r.pattern[s][i] = basePattern.pattern[s][i];
					}
				}
				
				SDR	contextSDR = originalRythmSDRs.get(s);
				if (s==(stepsPerPattern / 2) - 1) {
					mixIncrementPerStep = (mixEnd - mixMid) / (stepsPerPattern / 2);
					bound = mixEnd;
				}
				if (mix>0) {
					int max = stepsPerPattern - (int)(mix * stepsPerPattern);
					if (maintainBeatFactor>1 && s % rythm.stepsPerBeat == 0) {
						max = (int) (max * maintainBeatFactor);
					}
					if (max <= stepsPerPattern && (max == 0 || Rand.getRandomInt(0, max)==0)) {
						contextSDR = rythmSDRs.get(s);
					}
				}
				
				SDR patternSDR = InstrumentPattern.getSDRForPatternStep(s, values);
				NetworkIO networkIO = new NetworkIO();
				networkIO.setValue(NetworkConfigFactory.CONTEXT_INPUT, contextSDR);
				networkIO.setValue(NetworkConfigFactory.PATTERN_INPUT, patternSDR);
				network.processIO(networkIO);
				if (networkIO.hasErrors()) {
					Logger.err(this, networkIO.getErrors().get(0));
					break;
				}
				workingIO = networkIO;
				if (mixIncrementPerStep>0) {
					mix += mixIncrementPerStep;
					if (mix > bound) {
						mix = bound;
					}
				} else {
					mix -= (mixIncrementPerStep * -1);
					if (mix < bound) {
						mix = bound;
					}
				}
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
		int r[] = new int[InstrumentPattern.INSTRUMENT_NAMES.length];
		SortedMap<String,Object> predictedValues = prevIO.getClassificationValues(1);
		for (int i = 0; i < InstrumentPattern.INSTRUMENT_NAMES.length; i++) {
			Object value = predictedValues.get(InstrumentPattern.INSTRUMENT_NAMES[i] + "Classifier");
			if (value==null) {
				value = InstrumentPattern.OFF;
			}
			r[i] = (int) value;
		}
		return r;
	}
}
