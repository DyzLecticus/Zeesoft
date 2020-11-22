package nl.zeesoft.zdbd.pattern;

public class PatternFactory {
	public static InstrumentPattern getFourOnFloorInstrumentPattern(int patternNum) {
		Rythm rythm = new Rythm();
		
		InstrumentPattern pattern = new InstrumentPattern();
		pattern.initialize(rythm);
		pattern.num = patternNum;
	
		pattern.setKick(0,InstrumentPattern.ACCENT);
		pattern.setKick(4,InstrumentPattern.ACCENT);
		pattern.setKick(8,InstrumentPattern.ACCENT);
		pattern.setKick(12,InstrumentPattern.ACCENT);
		if (patternNum==1 || patternNum==2) {
			pattern.setKick(14,InstrumentPattern.ON);
		}

		pattern.setSnare(4,InstrumentPattern.ACCENT);
		if (patternNum==2) {
			pattern.setSnare(9,InstrumentPattern.ON);
		}
		pattern.setSnare(12,InstrumentPattern.ACCENT);
		if (patternNum==1 || patternNum==2) {
			pattern.setSnare(15,InstrumentPattern.ON);
		}

		pattern.setHihat(0,false,InstrumentPattern.ACCENT);
		pattern.setHihat(1,false,InstrumentPattern.ON);
		pattern.setHihat(2,true,InstrumentPattern.ACCENT);
		pattern.setHihat(3,false,InstrumentPattern.ON);
		pattern.setHihat(4,false,InstrumentPattern.ACCENT);
		pattern.setHihat(5,false,InstrumentPattern.ON);
		pattern.setHihat(6,true,InstrumentPattern.ACCENT);
		pattern.setHihat(7,false,InstrumentPattern.ON);
		pattern.setHihat(8,false,InstrumentPattern.ACCENT);
		pattern.setHihat(9,false,InstrumentPattern.ON);
		pattern.setHihat(10,true,InstrumentPattern.ACCENT);
		pattern.setHihat(11,false,InstrumentPattern.ON);
		pattern.setHihat(12,false,InstrumentPattern.ACCENT);
		pattern.setHihat(13,false,InstrumentPattern.ON);
		pattern.setHihat(14,true,InstrumentPattern.ACCENT);
		pattern.setHihat(15,false,InstrumentPattern.ON);
		
		pattern.setRide(0,InstrumentPattern.ACCENT);
		pattern.setRide(4,InstrumentPattern.ACCENT);
		pattern.setRide(8,InstrumentPattern.ACCENT);
		pattern.setRide(12,InstrumentPattern.ACCENT);
		pattern.setRide(14,InstrumentPattern.ON);

		if (patternNum==0) {
			pattern.setCymbal(0,InstrumentPattern.ACCENT);
		}
		if (patternNum==2 || patternNum==3) {
			pattern.setCymbal(12,InstrumentPattern.ACCENT);
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

	public static PatternSequence getFourOnFloorInstrumentPatternSequence() {
		PatternSequence seq = new PatternSequence();
		seq.patterns.add(getFourOnFloorInstrumentPattern(0));
		seq.patterns.add(getFourOnFloorInstrumentPattern(1));
		seq.patterns.add(getFourOnFloorInstrumentPattern(2));
		return seq;
	}
}
