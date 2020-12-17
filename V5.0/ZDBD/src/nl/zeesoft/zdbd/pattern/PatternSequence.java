package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdbd.ThemeControllerSettings;
import nl.zeesoft.zdbd.midi.convertors.PatternSequenceConvertor;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class PatternSequence {
	public Rythm						rythm		= new Rythm();
	public List<InstrumentPattern>		patterns	= new ArrayList<InstrumentPattern>();
	public int[]						sequence	= new int[4];
	
	public PatternSequence() {
		clear();
	}
	
	public PatternSequence copy() {
		PatternSequence r = new PatternSequence();
		r.copyFrom(this);
		return r;
	}
	
	public void copyFrom(PatternSequence seq) {
		this.rythm.copyFrom(seq.rythm);
		this.patterns.clear();
		for (InstrumentPattern pat: seq.patterns) {
			patterns.add(pat.copy());
		}
		for (int i = 0; i < this.sequence.length; i++) {
			this.sequence[i] = seq.sequence[i];
		}
	}
	
	public void clear() {
		patterns.clear();
		sequence[0] = -1;
		sequence[1] = -1;
		sequence[2] = -1;
		sequence[3] = -1;
	}
	
	public ThemeControllerSettings fromFile(String path) {
		return (ThemeControllerSettings) PersistableCollection.fromFile(path);
	}
	
	public void toFile(String path) {
		PersistableCollection.toFile(this, path);
	}

	public List<InstrumentPattern> getSequencedPatterns() {
		List<InstrumentPattern> r = new ArrayList<InstrumentPattern>();
		for (int i = 0; i < sequence.length; i++) {
			if (sequence[i]>= 0 && sequence[i] < patterns.size()) {
				r.add(patterns.get(sequence[i]));
			}
		}
		return r;
	}

	public int getTotalSteps() {
		return getSequencedPatterns().size() * rythm.getStepsPerPattern();
	}
	
	public List<NetworkIO> getNetworkIO() {
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		List<InstrumentPattern> patterns = getSequencedPatterns();
		for (InstrumentPattern pattern: patterns) {
			int stepsPerPattern = rythm.getStepsPerPattern();
			for (int s = 0; s < stepsPerPattern; s++) {
				NetworkIO io = new NetworkIO();
				io.setValue(NetworkConfigFactory.CONTEXT_INPUT, rythm.getSDRForPatternStep(pattern.num, s));
				io.setValue(NetworkConfigFactory.GROUP1_INPUT, pattern.getSDRForGroup1Step(s));
				io.setValue(NetworkConfigFactory.GROUP2_INPUT, pattern.getSDRForGroup2Step(s));
				r.add(io);
			}
		}
		return r;
	}
	
	public Sequence toDefaultMidiSequence() {
		PatternSequenceConvertor convertor = new PatternSequenceConvertor();
		return convertor.generateSequenceForPatternSequence(this);
	}
}
