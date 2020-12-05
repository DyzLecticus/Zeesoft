package nl.zeesoft.zdbd.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class PatternGenerator {
	public NetworkIO		prevIO				= null;
	
	// Network merger dirsortion controls
	public float			group1Distortion	= 0.1F; // 0 - 1
	public float			group2Distortion	= 0.1F; // 0 - 1
	
	// Randomized rythm generation controls
	public int				smallerChunk		= 1;
	public int				largerChunk			= 2;
	public boolean			randomChunkOffset	= true;
	
	// Mix controls
	public String[]			skipInstruments		= new String[0];
	public float			mixStart			= 0.2F; // 0 - 1
	public float			mixEnd				= 1.0F; // 0 - 1
	public float			maintainBeat		= 1.0F; // 0 - 1
	public boolean			maintainFeedback	= false;
	
	// System resource usage limiting
	public int				sleepMs				= 50;

	public void setSkipInstruments(String ... names) {
		skipInstruments = names;
	}
	
	public PatternSequence generatePatternSequence(Network network, PatternSequence sequence) {
		Logger.dbg(this, new Str("Generating sequence ..."));
		PatternSequence r = new PatternSequence();
		for (InstrumentPattern pattern: sequence.patterns) {
			r.patterns.add(generatePattern(network,sequence,pattern.num));
		}
		for (int p = 0; p < r.sequence.length; p++) {
			r.sequence[p] = sequence.sequence[p];
		}
		Logger.dbg(this, new Str("Generated sequence"));
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
		r.rythm.copyFrom(rythm);
		if (basePattern!=null) {
			r.num = basePattern.num; 
		}
		
		if (prevIO!=null) {
			NetworkIO workingIO = prevIO;
			network.setProcessorProperty(NetworkConfigFactory.GROUP1_INPUT + "Merger", "distortion", group1Distortion);
			network.setProcessorProperty(NetworkConfigFactory.GROUP2_INPUT + "Merger", "distortion", group2Distortion);
			
			List<SDR> originalRythmSDRs = rythm.getSDRsForPattern(r.num);
			
			// Generate pattern
			int stepsPerPattern = rythm.getStepsPerPattern();
			float mix = mixStart;
			float mixIncrementPerBeat = (mixEnd - mixStart) / rythm.beatsPerPattern;
			boolean original = (mix == 0);
			for (int s = 0 ; s < stepsPerPattern; s++) {
				if (s % rythm.stepsPerBeat == 0) {
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
				if (!ori && s % rythm.stepsPerBeat == 0 && maintainBeat>0) {
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
				
				if (sleepMs>0) {
					try {
						Thread.sleep(sleepMs);
					} catch (InterruptedException e) {
						Logger.err(this, new Str("Pattern generator was interrupted"),e);
					}
				}
			}
			
			network.setProcessorProperty(NetworkConfigFactory.GROUP1_INPUT + "Merger", "distortion", 0F);
			network.setProcessorProperty(NetworkConfigFactory.GROUP2_INPUT + "Merger", "distortion", 0F);
		} else {
			Logger.err(this, new Str("Previous network IO has not been specified"));
		}
		return r;
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
	
	protected int[] addPredictedValuesToPattern(InstrumentPattern pattern, int step, NetworkIO workingIO, InstrumentPattern basePattern, boolean ori) {
		int values[] = getPredictedValuesFromPreviousIO(workingIO);
		for (PatternInstrument inst: pattern.instruments) {
			if (basePattern!=null && (ori || skipInstrumentsContains(inst.name()))) {
				inst.stepValues[step] = basePattern.getInstrument(inst.name()).stepValues[step];
				if (maintainFeedback) {
					values[inst.index] = inst.stepValues[step];
				}
			} else {
				inst.stepValues[step] = values[inst.index];
			}
		}
		return values;
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
