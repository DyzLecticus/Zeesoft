package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class PatternFactory {
	public static DrumAndBassPattern getFourOnFloorDrumAndBassPattern(int patternNum) {
		Rythm rythm = new Rythm();
		
		DrumAndBassPattern pattern = new DrumAndBassPattern();
		pattern.initialize(rythm);
		pattern.num = patternNum;
	
		pattern.setDrumNote(0,DrumAndBassPattern.BASEBEAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(4,DrumAndBassPattern.BASEBEAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(8,DrumAndBassPattern.BASEBEAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(12,DrumAndBassPattern.BASEBEAT,DrumAndBassPattern.ACCENT);
		if (patternNum==1 || patternNum==2) {
			pattern.setDrumNote(14,DrumAndBassPattern.BASEBEAT,DrumAndBassPattern.ON);
		}

		pattern.setDrumNote(0,DrumAndBassPattern.SNARE,DrumAndBassPattern.ACCENT);
		if (patternNum==2) {
			pattern.setDrumNote(9,DrumAndBassPattern.SNARE,DrumAndBassPattern.ON);
		}
		pattern.setDrumNote(12,DrumAndBassPattern.SNARE,DrumAndBassPattern.ACCENT);
		if (patternNum==1 || patternNum==2) {
			pattern.setDrumNote(15,DrumAndBassPattern.SNARE,DrumAndBassPattern.ON);
		}

		pattern.setDrumNote(0,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(1,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ON);
		pattern.setDrumNote(3,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ON);
		pattern.setDrumNote(4,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(5,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ON);
		pattern.setDrumNote(7,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ON);
		pattern.setDrumNote(8,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(9,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ON);
		pattern.setDrumNote(11,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ON);
		pattern.setDrumNote(12,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(13,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ON);
		pattern.setDrumNote(15,DrumAndBassPattern.CLOSED_HIHAT,DrumAndBassPattern.ON);

		pattern.setDrumNote(2,DrumAndBassPattern.OPEN_HIHAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(6,DrumAndBassPattern.OPEN_HIHAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(10,DrumAndBassPattern.OPEN_HIHAT,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(14,DrumAndBassPattern.OPEN_HIHAT,DrumAndBassPattern.ACCENT);
		
		pattern.setDrumNote(0,DrumAndBassPattern.RIDE,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(4,DrumAndBassPattern.RIDE,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(8,DrumAndBassPattern.RIDE,DrumAndBassPattern.ACCENT);
		pattern.setDrumNote(12,DrumAndBassPattern.RIDE,DrumAndBassPattern.RIDE);
		pattern.setDrumNote(14,DrumAndBassPattern.RIDE,DrumAndBassPattern.ON);

		if (patternNum==0) {
			pattern.setDrumNote(0,DrumAndBassPattern.CYMBAL,DrumAndBassPattern.ACCENT);
		}
		if (patternNum==2 || patternNum==3) {
			pattern.setDrumNote(12,DrumAndBassPattern.CYMBAL,DrumAndBassPattern.ACCENT);
		}
		
		pattern.setBassNote(2, 1, true);
		pattern.setBassNote(3, 1, true);
		pattern.setBassNote(6, 1, true);
		pattern.setBassNote(7, 1, true);
		pattern.setBassNote(10, 1, true);
		pattern.setBassNote(11, 1, true);
		pattern.setBassNote(14, 2, true);
		return pattern;
	}

	public static Sequence getFourOnFloorDrumAndBassSequence() {
		return new Sequence();
	}

	public static List<DrumAndBassPattern> getFourOnFloorDrumAndBassPatternSequence() {
		List<DrumAndBassPattern> r = new ArrayList<DrumAndBassPattern>();
		Sequence seq = getFourOnFloorDrumAndBassSequence();
		for (int i = 0; i < seq.patterns.length; i++) {
			r.add(getFourOnFloorDrumAndBassPattern(seq.patterns[i]));
		}
		return r;
	}
	
	public static List<NetworkIO> getFourOnFloorDrumAndBassIO() {
		return getNetworkIOForPatternSequence(getFourOnFloorDrumAndBassPatternSequence());
	}
	
	public static List<NetworkIO> getNetworkIOForPatternSequence(List<DrumAndBassPattern> patterns) {
		List<NetworkIO> r = new ArrayList<NetworkIO>();

		Rythm rythm = patterns.get(0).rythm;
		int totalSteps = rythm.getStepsPerPattern() * patterns.size();
		
		for (int i = 0; i < totalSteps; i++) {
			r.add(new NetworkIO());
		}
		
		int p = 0;
		for (DrumAndBassPattern pattern: patterns) {
			int start = p * rythm.getStepsPerPattern();
			
			List<SDR> rythmSDRs = rythm.getSDRsForPattern(pattern.num);
			int ps = 0;
			for (int s = start; s < start + rythm.getStepsPerPattern(); s++) {
				NetworkIO io = r.get(s);
				io.setValue(NetworkConfigFactory.CONTEXT_INPUT, rythmSDRs.get(ps));
				ps++;
			}
			
			List<SDR> patternSDRs = pattern.getSDRsForPattern();
			ps = 0;
			for (int s = start; s < start + rythm.getStepsPerPattern(); s++) {
				NetworkIO io = r.get(s);
				io.setValue(NetworkConfigFactory.PATTERN_INPUT, patternSDRs.get(ps));
				ps++;
			}
			
			p++;
		}
		return r;
	}
}
