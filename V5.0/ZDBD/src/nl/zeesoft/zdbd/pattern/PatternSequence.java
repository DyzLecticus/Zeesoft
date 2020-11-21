package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class PatternSequence {
	public List<InstrumentPattern>		patterns	= new ArrayList<InstrumentPattern>();
	public int[]						sequence	= new int[4];
	
	public PatternSequence() {
		sequence[0] = 0;
		sequence[1] = 1;
		sequence[2] = 0;
		sequence[3] = 2;
	}

	public List<InstrumentPattern> getSequencedPatterns() {
		List<InstrumentPattern> r = new ArrayList<InstrumentPattern>();
		for (int i = 0; i < sequence.length; i++) {
			if (sequence[i] < patterns.size()) {
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
				io.setValue(NetworkConfigFactory.CONTEXT_INPUT, pattern.rythm.getSDRForPatternStep(pattern.num, s));
				io.setValue(NetworkConfigFactory.PATTERN_INPUT, pattern.getSDRForPatternStep(s));
				r.add(io);
			}
		}
		return r;
	}
}
