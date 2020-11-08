package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class PatternFactory {
	public static DrumPattern getFourOnFloor(int patternNum) {
		Rythm rythm = new Rythm();
		
		DrumPattern pattern = new DrumPattern();
		pattern.initialize(rythm);
		pattern.num = patternNum;
				
		pattern.drums[0][DrumPattern.BASEBEAT] = DrumPattern.ACCENT;
		pattern.drums[4][DrumPattern.BASEBEAT] = DrumPattern.ACCENT;
		pattern.drums[8][DrumPattern.BASEBEAT] = DrumPattern.ACCENT;
		pattern.drums[12][DrumPattern.BASEBEAT] = DrumPattern.ACCENT;
		if (patternNum==1 || patternNum==2) {
			pattern.drums[14][DrumPattern.BASEBEAT] = DrumPattern.ON;
		}
		
		pattern.drums[4][DrumPattern.SNARE] = DrumPattern.ACCENT;
		if (patternNum==2) {
			pattern.drums[9][DrumPattern.SNARE] = DrumPattern.ON;
		}
		pattern.drums[12][DrumPattern.SNARE] = DrumPattern.ACCENT;
		if (patternNum==1 || patternNum==2) {
			pattern.drums[15][DrumPattern.SNARE] = DrumPattern.ON;
		}
		
		pattern.drums[0][DrumPattern.CLOSED_HIHAT] = DrumPattern.ACCENT;
		pattern.drums[1][DrumPattern.CLOSED_HIHAT] = DrumPattern.ON;
		pattern.drums[3][DrumPattern.CLOSED_HIHAT] = DrumPattern.ON;
		pattern.drums[4][DrumPattern.CLOSED_HIHAT] = DrumPattern.ACCENT;
		pattern.drums[5][DrumPattern.CLOSED_HIHAT] = DrumPattern.ON;
		pattern.drums[7][DrumPattern.CLOSED_HIHAT] = DrumPattern.ON;
		pattern.drums[8][DrumPattern.CLOSED_HIHAT] = DrumPattern.ACCENT;
		pattern.drums[9][DrumPattern.CLOSED_HIHAT] = DrumPattern.ON;
		pattern.drums[11][DrumPattern.CLOSED_HIHAT] = DrumPattern.ON;
		pattern.drums[12][DrumPattern.CLOSED_HIHAT] = DrumPattern.ACCENT;
		pattern.drums[13][DrumPattern.CLOSED_HIHAT] = DrumPattern.ON;
		pattern.drums[15][DrumPattern.CLOSED_HIHAT] = DrumPattern.ON;
		
		pattern.drums[2][DrumPattern.OPEN_HIHAT] = DrumPattern.ACCENT;
		pattern.drums[6][DrumPattern.OPEN_HIHAT] = DrumPattern.ACCENT;
		pattern.drums[10][DrumPattern.OPEN_HIHAT] = DrumPattern.ACCENT;
		pattern.drums[14][DrumPattern.OPEN_HIHAT] = DrumPattern.ACCENT;

		return pattern;
	}
	
	public static List<NetworkIO> getFourOnFloorIO() {
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		
		Rythm rythm = new Rythm();
		int totalSteps = rythm.getStepsPerPattern() * 4;
		
		for (int i = 0; i < totalSteps; i++) {
			r.add(new NetworkIO());
		}
		
		for (int p = 0; p < 4; p++) {
			
			int patternNum = p;
			if (p==2) {
				patternNum = 0;
			} else if (p==3) {
				patternNum = 2;
			}
			
			int start = p * rythm.getStepsPerPattern();
			
			List<SDR> rythmSDRs = rythm.getSDRsForPattern(patternNum);
			int ps = 0;
			for (int s = start; s < start + rythm.getStepsPerPattern(); s++) {
				NetworkIO io = r.get(s);
				io.setValue(NetworkConfigFactory.CONTEXT_INPUT, rythmSDRs.get(ps));
				ps++;
			}
			
			DrumPattern pattern = getFourOnFloor(patternNum);
			List<SDR> patternSDRs = pattern.getSDRsForPattern();
			ps = 0;
			for (int s = start; s < start + rythm.getStepsPerPattern(); s++) {
				NetworkIO io = r.get(s);
				io.setValue(NetworkConfigFactory.PATTERN_INPUT, patternSDRs.get(ps));
				ps++;
			}
		}
		return r;
	}
	
	public static DrumAndBassPattern getFourOnFloorDrumAndBassPattern(int patternNum) {
		Rythm rythm = new Rythm();
		
		DrumAndBassPattern pattern = new DrumAndBassPattern();
		pattern.initialize(rythm);
		pattern.num = patternNum;
	
		pattern.setDrumNote(0,DrumPattern.BASEBEAT,DrumPattern.ACCENT);
		pattern.setDrumNote(4,DrumPattern.BASEBEAT,DrumPattern.ACCENT);
		pattern.setDrumNote(8,DrumPattern.BASEBEAT,DrumPattern.ACCENT);
		pattern.setDrumNote(12,DrumPattern.BASEBEAT,DrumPattern.ACCENT);
		if (patternNum==1 || patternNum==2) {
			pattern.setDrumNote(14,DrumPattern.BASEBEAT,DrumPattern.ON);
		}

		pattern.setDrumNote(0,DrumPattern.SNARE,DrumPattern.ACCENT);
		if (patternNum==2) {
			pattern.setDrumNote(9,DrumPattern.SNARE,DrumPattern.ON);
		}
		pattern.setDrumNote(12,DrumPattern.SNARE,DrumPattern.ACCENT);
		if (patternNum==1 || patternNum==2) {
			pattern.setDrumNote(15,DrumPattern.SNARE,DrumPattern.ON);
		}

		pattern.setDrumNote(0,DrumPattern.CLOSED_HIHAT,DrumPattern.ACCENT);
		pattern.setDrumNote(1,DrumPattern.CLOSED_HIHAT,DrumPattern.ON);
		pattern.setDrumNote(3,DrumPattern.CLOSED_HIHAT,DrumPattern.ON);
		pattern.setDrumNote(4,DrumPattern.CLOSED_HIHAT,DrumPattern.ACCENT);
		pattern.setDrumNote(5,DrumPattern.CLOSED_HIHAT,DrumPattern.ON);
		pattern.setDrumNote(7,DrumPattern.CLOSED_HIHAT,DrumPattern.ON);
		pattern.setDrumNote(8,DrumPattern.CLOSED_HIHAT,DrumPattern.ACCENT);
		pattern.setDrumNote(9,DrumPattern.CLOSED_HIHAT,DrumPattern.ON);
		pattern.setDrumNote(11,DrumPattern.CLOSED_HIHAT,DrumPattern.ON);
		pattern.setDrumNote(12,DrumPattern.CLOSED_HIHAT,DrumPattern.ACCENT);
		pattern.setDrumNote(13,DrumPattern.CLOSED_HIHAT,DrumPattern.ON);
		pattern.setDrumNote(15,DrumPattern.CLOSED_HIHAT,DrumPattern.ON);

		pattern.setDrumNote(2,DrumPattern.OPEN_HIHAT,DrumPattern.ACCENT);
		pattern.setDrumNote(6,DrumPattern.OPEN_HIHAT,DrumPattern.ACCENT);
		pattern.setDrumNote(10,DrumPattern.OPEN_HIHAT,DrumPattern.ACCENT);
		pattern.setDrumNote(14,DrumPattern.OPEN_HIHAT,DrumPattern.ACCENT);
		
		pattern.setDrumNote(0,DrumPattern.RIDE,DrumPattern.ACCENT);
		pattern.setDrumNote(4,DrumPattern.RIDE,DrumPattern.ACCENT);
		pattern.setDrumNote(8,DrumPattern.RIDE,DrumPattern.ACCENT);
		pattern.setDrumNote(12,DrumPattern.RIDE,DrumPattern.RIDE);
		pattern.setDrumNote(14,DrumPattern.RIDE,DrumPattern.ON);

		if (patternNum==0) {
			pattern.setDrumNote(0,DrumPattern.CYMBAL,DrumPattern.ACCENT);
		}
		if (patternNum==2 || patternNum==3) {
			pattern.setDrumNote(12,DrumPattern.CYMBAL,DrumPattern.ACCENT);
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
	
	public static List<NetworkIO> getFourOnFloorDrumAndBassIO() {
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		
		Rythm rythm = new Rythm();
		int totalSteps = rythm.getStepsPerPattern() * 4;
		
		for (int i = 0; i < totalSteps; i++) {
			r.add(new NetworkIO());
		}
		
		for (int p = 0; p < 4; p++) {
			
			int patternNum = p;
			if (p==2) {
				patternNum = 0;
			} else if (p==3) {
				patternNum = 2;
			}
			
			int start = p * rythm.getStepsPerPattern();
			
			List<SDR> rythmSDRs = rythm.getSDRsForPattern(patternNum);
			int ps = 0;
			for (int s = start; s < start + rythm.getStepsPerPattern(); s++) {
				NetworkIO io = r.get(s);
				io.setValue(NetworkConfigFactory.CONTEXT_INPUT, rythmSDRs.get(ps));
				ps++;
			}
			
			DrumAndBassPattern pattern = getFourOnFloorDrumAndBassPattern(patternNum);
			List<SDR> patternSDRs = pattern.getSDRsForPattern();
			ps = 0;
			for (int s = start; s < start + rythm.getStepsPerPattern(); s++) {
				NetworkIO io = r.get(s);
				io.setValue(NetworkConfigFactory.PATTERN_INPUT, patternSDRs.get(ps));
				ps++;
			}
		}
		return r;
	}
}
