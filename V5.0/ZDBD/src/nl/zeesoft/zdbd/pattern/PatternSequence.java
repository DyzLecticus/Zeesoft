package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.Settings;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class PatternSequence {
	public List<InstrumentPattern>		patterns	= new ArrayList<InstrumentPattern>();
	public int[]						sequence	= new int[4];
	
	public PatternSequence() {
		sequence[0] = -1;
		sequence[1] = -1;
		sequence[2] = -1;
		sequence[3] = -1;
	}
	
	public PatternSequence copy() {
		PatternSequence r = new PatternSequence();
		r.copyFrom(this);
		return r;
	}
	
	public void copyFrom(PatternSequence seq) {
		this.patterns.clear();
		for (InstrumentPattern pat: seq.patterns) {
			patterns.add(pat.copy());
		}
		for (int i = 0; i < this.sequence.length; i++) {
			this.sequence[i] = seq.sequence[i];
		}
	}
	
	public Settings fromFile(String path) {
		return (Settings) PersistableCollection.fromFile(path);
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
		int r = 0;
		List<InstrumentPattern> patterns = getSequencedPatterns();
		for (InstrumentPattern pattern: patterns) {
			r += pattern.rythm.getStepsPerPattern();
		}
		return r;
	}
	
	public List<NetworkIO> getNetworkIO() {
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		List<InstrumentPattern> patterns = getSequencedPatterns();
		for (InstrumentPattern pattern: patterns) {
			int stepsPerPattern = pattern.rythm.getStepsPerPattern();
			for (int s = 0; s < stepsPerPattern; s++) {
				NetworkIO io = new NetworkIO();
				io.setValue(NetworkConfigFactory.RYTHM_INPUT, pattern.rythm.getSDRForPatternStep(pattern.num, s));
				io.setValue(NetworkConfigFactory.GROUP1_INPUT, pattern.getSDRForGroup1Step(s));
				io.setValue(NetworkConfigFactory.GROUP2_INPUT, pattern.getSDRForGroup2Step(s));
				r.add(io);
			}
		}
		return r;
	}
}
