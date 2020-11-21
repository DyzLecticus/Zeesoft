package nl.zeesoft.zdbd.pattern;

public class PatternFactory {
	public static InstrumentPattern getFourOnFloorDrumAndBassPattern(int patternNum) {
		Rythm rythm = new Rythm();
		
		InstrumentPattern pattern = new InstrumentPattern();
		pattern.initialize(rythm);
		pattern.num = patternNum;
	
		pattern.setDrumNote(0,InstrumentPattern.BASEBEAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(4,InstrumentPattern.BASEBEAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(8,InstrumentPattern.BASEBEAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(12,InstrumentPattern.BASEBEAT,InstrumentPattern.ACCENT);
		if (patternNum==1 || patternNum==2) {
			pattern.setDrumNote(14,InstrumentPattern.BASEBEAT,InstrumentPattern.ON);
		}

		pattern.setDrumNote(4,InstrumentPattern.SNARE,InstrumentPattern.ACCENT);
		if (patternNum==2) {
			pattern.setDrumNote(9,InstrumentPattern.SNARE,InstrumentPattern.ON);
		}
		pattern.setDrumNote(12,InstrumentPattern.SNARE,InstrumentPattern.ACCENT);
		if (patternNum==1 || patternNum==2) {
			pattern.setDrumNote(15,InstrumentPattern.SNARE,InstrumentPattern.ON);
		}

		pattern.setDrumNote(0,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(1,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ON);
		pattern.setDrumNote(3,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ON);
		pattern.setDrumNote(4,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(5,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ON);
		pattern.setDrumNote(7,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ON);
		pattern.setDrumNote(8,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(9,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ON);
		pattern.setDrumNote(11,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ON);
		pattern.setDrumNote(12,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(13,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ON);
		pattern.setDrumNote(15,InstrumentPattern.CLOSED_HIHAT,InstrumentPattern.ON);

		pattern.setDrumNote(2,InstrumentPattern.OPEN_HIHAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(6,InstrumentPattern.OPEN_HIHAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(10,InstrumentPattern.OPEN_HIHAT,InstrumentPattern.ACCENT);
		pattern.setDrumNote(14,InstrumentPattern.OPEN_HIHAT,InstrumentPattern.ACCENT);
		
		pattern.setDrumNote(0,InstrumentPattern.RIDE,InstrumentPattern.ACCENT);
		pattern.setDrumNote(4,InstrumentPattern.RIDE,InstrumentPattern.ACCENT);
		pattern.setDrumNote(8,InstrumentPattern.RIDE,InstrumentPattern.ACCENT);
		pattern.setDrumNote(12,InstrumentPattern.RIDE,InstrumentPattern.RIDE);
		pattern.setDrumNote(14,InstrumentPattern.RIDE,InstrumentPattern.ON);

		if (patternNum==0) {
			pattern.setDrumNote(0,InstrumentPattern.CYMBAL,InstrumentPattern.ACCENT);
		}
		if (patternNum==2 || patternNum==3) {
			pattern.setDrumNote(12,InstrumentPattern.CYMBAL,InstrumentPattern.ACCENT);
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

	public static PatternSequence getFourOnFloorDrumAndBassPatternSequence() {
		PatternSequence seq = new PatternSequence();
		seq.patterns.add(getFourOnFloorDrumAndBassPattern(0));
		seq.patterns.add(getFourOnFloorDrumAndBassPattern(1));
		seq.patterns.add(getFourOnFloorDrumAndBassPattern(2));
		return seq;
	}
}
