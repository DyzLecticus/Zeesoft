package nl.zeesoft.zdbd.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.instruments.Note;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class Generator {
	public String			name						= "";
	
	// Network merger dirsortion controls
	public float			group1Distortion			= 0.1F; // 0 - 1
	public float			group2Distortion			= 0.0F; // 0 - 1
	
	// Randomized rythm generation controls
	public int				smallerChunk				= 1;
	public int				largerChunk					= 2;
	public boolean			randomChunkOffset			= true;
	
	// Mix controls
	public String[]			skipInstruments				= {Note.NAME};
	public float			mixStart					= 0.0F; // 0 - 1
	public float			mixEnd						= 1.0F; // 0 - 1
	public float			maintainBeat				= 1.0F; // 0 - 1
	public boolean			maintainFeedback			= false;
	
	// Output
	public PatternSequence	generatedPatternSequence	= null;
	
	public Generator copy() {
		Generator r = new Generator();
		r.copyFrom(this);
		return r;
	}

	public void copyFrom(Generator gen) {
		this.name = gen.name;
		this.group1Distortion = gen.group1Distortion;
		this.group2Distortion = gen.group2Distortion;
		this.smallerChunk = gen.smallerChunk;
		this.largerChunk = gen.largerChunk;
		this.randomChunkOffset = gen.randomChunkOffset;
		this.setSkipInstruments(gen.skipInstruments);
		this.mixStart = gen.mixStart;
		this.mixEnd = gen.mixEnd;
		this.maintainBeat = gen.maintainBeat;
		this.maintainFeedback = gen.maintainFeedback;
		if (gen.generatedPatternSequence!=null) {
			this.generatedPatternSequence = gen.generatedPatternSequence.copy();
		} else {
			this.generatedPatternSequence = null;
		}
	}

	public void setSkipInstruments(String ... names) {
		skipInstruments = new String[names.length];
		for (int n = 0; n < names.length; n++) {
			skipInstruments[n] = names[n];
		}
	}
	
	public void generatePatternSequence(Network network, NetworkIO lastIO, PatternSequence trainingSequence) {
		if (lastIO!=null) {
			Logger.dbg(this, new Str("Generating sequence ..."));
			network.setSequential(true);
			network.setProcessorProperty(NetworkConfigFactory.GROUP1_INPUT + "Merger", "distortion", group1Distortion);
			network.setProcessorProperty(NetworkConfigFactory.GROUP2_INPUT + "Merger", "distortion", group2Distortion);

			PatternSequence r = new PatternSequence();
			r.rythm.copyFrom(trainingSequence.rythm);
			for (InstrumentPattern pattern: trainingSequence.patterns) {
				List<SDR> rythmSDRs = generateRythmSDRs(trainingSequence,pattern.num);
				r.patterns.add(generatePattern(network,lastIO,rythmSDRs,trainingSequence,pattern.num));
			}
			for (int p = 0; p < r.sequence.length; p++) {
				r.sequence[p] = trainingSequence.sequence[p];
			}
			generatedPatternSequence = r;
			
			network.setSequential(false);
			network.setProcessorProperty(NetworkConfigFactory.GROUP1_INPUT + "Merger", "distortion", 0F);
			network.setProcessorProperty(NetworkConfigFactory.GROUP2_INPUT + "Merger", "distortion", 0F);
			Logger.dbg(this, new Str("Generated sequence"));
		}
	}
	
	protected InstrumentPattern generatePattern(Network network, NetworkIO lastIO, List<SDR> rythmSDRs, PatternSequence sequence, int patternNum) {
		InstrumentPattern r = new InstrumentPattern();
		InstrumentPattern basePattern = sequence.patterns.get(patternNum);
		if (basePattern!=null) {
			r.num = basePattern.num; 
		}
		
		if (lastIO!=null) {
			NetworkIO workingIO = lastIO;
			List<SDR> originalRythmSDRs = sequence.rythm.getSDRsForPattern(r.num);
			int stepsPerPattern = sequence.rythm.getStepsPerPattern();
			float mix = mixStart;
			float mixIncrementPerBeat = (mixEnd - mixStart) / sequence.rythm.beatsPerPattern;
			boolean original = (mix == 0);
			for (int s = 0 ; s < stepsPerPattern; s++) {
				if (s % sequence.rythm.stepsPerBeat == 0) {
					if (mixIncrementPerBeat>0) {
						mix += mixIncrementPerBeat;
					} else if (mixIncrementPerBeat<0) {
						mix -= (mixIncrementPerBeat * -1);
					}
					if (mix > 0) {
						if (mix==1 || Rand.getRandomInt(0, 100)<(int)(100F * mix)) {
							original = false;
						}
					} else {
						original = true; 
					}
				}

				boolean ori = original;
				if (!ori && s % sequence.rythm.stepsPerBeat == 0 && maintainBeat>0) {
					if (maintainBeat==1 || Rand.getRandomInt(0, 100)<(int)(100F * maintainBeat)) {
						ori = true;
					}
				}
				
				int[] values = addPredictedValuesToPattern(r, s, workingIO, basePattern, ori);
				
				SDR	rythmSDR = originalRythmSDRs.get(s);
				if (!ori) {
					rythmSDR = rythmSDRs.get(s);
				}

				InstrumentPattern t = new InstrumentPattern();
				for (PatternInstrument inst: t.instruments) {
					inst.stepValues[0] = values[inst.index];
				}
				SDR group1SDR = t.getSDRForGroup1Step(0);
				SDR group2SDR = t.getSDRForGroup2Step(0);
				
				NetworkIO networkIO = new NetworkIO();
				networkIO.setValue(NetworkConfigFactory.CONTEXT_INPUT, rythmSDR);
				networkIO.setValue(NetworkConfigFactory.GROUP1_INPUT, group1SDR);
				networkIO.setValue(NetworkConfigFactory.GROUP2_INPUT, group2SDR);
				network.processIO(networkIO);
				if (networkIO.hasErrors()) {
					Logger.err(this, networkIO.getErrors().get(0));
					break;
				}
				workingIO = networkIO;
			}
		} else {
			Logger.err(this, new Str("Previous network IO has not been specified"));
		}
		return r;
	}

	protected List<SDR> generateRythmSDRs(PatternSequence sequence, int patternNum) {
		List<SDR> r = new ArrayList<SDR>();
		
		List<Integer> chunkSizes = new ArrayList<Integer>();
		chunkSizes.add(sequence.rythm.stepsPerBeat);
		if (smallerChunk>0) {
			int size = sequence.rythm.stepsPerBeat - smallerChunk;
			if (size<1) {
				size = 1;
			}
			if (size < sequence.rythm.stepsPerBeat) {
				chunkSizes.add(sequence.rythm.stepsPerBeat - smallerChunk);
			}
		}
		if (largerChunk>0) {
			chunkSizes.add(sequence.rythm.stepsPerBeat + largerChunk);
		}
		
		int stepsPerPattern = sequence.rythm.getStepsPerPattern();
		int size = chunkSizes.get(Rand.getRandomInt(0, chunkSizes.size() - 1));
		for (int s = 0; s < stepsPerPattern; s++) {
			List<SDR> chunk = getRandomBeatChunk(sequence, patternNum, size);
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

	protected List<SDR> getRandomBeatChunk(PatternSequence sequence,int patternNum, int size) {
		List<SDR> r = new ArrayList<SDR>();
		if (patternNum == -1) {
			patternNum = Rand.getRandomInt(0, sequence.patterns.size() - 1);
		}
		InstrumentPattern pattern = sequence.patterns.get(patternNum);
		List<SDR> sdrs = sequence.rythm.getSDRsForPattern(pattern.num);
		int startBeat = Rand.getRandomInt(0, sequence.rythm.beatsPerPattern - 1);
		int step = startBeat * sequence.rythm.stepsPerBeat;
		if (randomChunkOffset) {
			step += Rand.getRandomInt(0, sequence.rythm.stepsPerBeat);
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
	
	protected int[] addPredictedValuesToPattern(InstrumentPattern pattern, int step, NetworkIO workingIO, InstrumentPattern basePattern, boolean ori) {
		int values[] = getPredictedValuesFromPreviousIO(workingIO);
		for (PatternInstrument inst: pattern.instruments) {
			if (basePattern!=null && (ori || skipInstrumentsContains(inst.name()))) {
				int value = basePattern.getInstrument(inst.name()).stepValues[step];
				if (value!=PatternInstrument.OFF) {
					inst.stepValues[step] = value;
					if (maintainFeedback) {
						values[inst.index] = inst.stepValues[step];
					}
				}
			} else {
				inst.stepValues[step] = values[inst.index];
			}
		}
		return values;
	}
	
	protected int[] getPredictedValuesFromPreviousIO(NetworkIO prevIO) {
		int r[] = new int[InstrumentPattern.INSTRUMENTS.size()];
		SortedMap<String,Object> predictedValues = prevIO.getClassificationValues(1);
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			Object value = predictedValues.get(inst.name() + "Classifier");
			if (value==null) {
				value = PatternInstrument.OFF;
			}
			r[inst.index] = (int) value;
		}
		return r;
	}
	
	protected boolean skipInstrumentsContains(String name) {
		boolean r = false;
		for (int i = 0; i < skipInstruments.length; i++) {
			if (skipInstruments[i].equals(name)) {
				r = true;
				break;
			}
		}
		return r;
	}
}
