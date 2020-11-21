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
	
	public float			contextDistortion	= 0.0F;
	public float			patternDistortion	= 0.5F;
	public float			combinedDistortion	= 0.0F;
	
	public int				smallerChunk		= 1;
	public int				largerChunk			= 2;
	public int				randomChunkOffset	= 3;
	public List<Integer>	skipInstruments		= new ArrayList<Integer>();

	public PatternSequence generatePatternSequence(Network network, PatternSequence sequence) {
		PatternSequence r = new PatternSequence();
		for (InstrumentPattern pattern: sequence.patterns) {
			r.patterns.add(generatePattern(network,sequence,null,pattern.num));
			// TODO Remove debug
			break;
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
		List<SDR> rythmSDRs = new ArrayList<SDR>();
		
		InstrumentPattern basePattern = null;
		if (baseSequencePattern>=0) {
			basePattern = sequence.patterns.get(baseSequencePattern);
		}
		if (rythm==null && basePattern!=null) {
			rythm = basePattern.rythm;
		}
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
				rythmSDRs.add(chunk.get(c));
				if (rythmSDRs.size()>=stepsPerPattern) {
					s = stepsPerPattern;
					break;
				}
				if (c>0) {
					s++;
				}
			}
			size = chunkSizes.get(Rand.getRandomInt(0, chunkSizes.size() - 1));
		}
		
		return generatePattern(network, rythm, rythmSDRs, basePattern);
	}

	protected List<SDR> getRandomBeatChunk(PatternSequence sequence, int size) {
		List<SDR> r = new ArrayList<SDR>();
		int num = Rand.getRandomInt(0, sequence.patterns.size() - 1);
		InstrumentPattern pattern = sequence.patterns.get(num);
		List<SDR> sdrs = pattern.rythm.getSDRsForPattern(pattern.num);
		int startBeat = Rand.getRandomInt(0, pattern.rythm.beatsPerPattern - 1);
		int step = startBeat * pattern.rythm.beatsPerPattern;
		if (randomChunkOffset > 0 && Rand.getRandomInt(0, randomChunkOffset)==randomChunkOffset) {
			step += (pattern.rythm.stepsPerBeat / 2);
		}
		for (int s = 0; s < size; s++) {
			r.add(sdrs.get(step));
			step++;
			if (step>=sdrs.size()) {
				step = 0;
			}
		}
		return r;
	}
	
	protected InstrumentPattern generatePattern(Network network, Rythm rythm, List<SDR> rythmSDRs, InstrumentPattern basePattern) {
		InstrumentPattern r = new InstrumentPattern();
		r.initialize(rythm);
		
		if (prevIO!=null) {
			network.setProcessorProperty(NetworkConfigFactory.CONTEXT_INPUT + "Merger", "distortion", contextDistortion);
			network.setProcessorProperty(NetworkConfigFactory.PATTERN_INPUT + "Merger", "distortion", patternDistortion);
			network.setProcessorProperty("Merger", "distortion", combinedDistortion);
			
			// Generate pattern
			int stepsPerPattern = rythm.getStepsPerPattern();
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
				SDR patternSDR = InstrumentPattern.getSDRForPatternStep(s, values);
				NetworkIO networkIO = new NetworkIO();
				networkIO.setValue(NetworkConfigFactory.CONTEXT_INPUT, contextSDR);
				networkIO.setValue(NetworkConfigFactory.PATTERN_INPUT, patternSDR);
				network.processIO(networkIO);
				if (networkIO.hasErrors()) {
					Logger.err(this, networkIO.getErrors().get(0));
					break;
				}
				prevIO = networkIO;
				
				// TODO Remove debug
				System.out.println("===========================");
				System.out.println(networkIO.toStr());
				System.out.println("===========================");
				if (s==1) {
					break;
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
