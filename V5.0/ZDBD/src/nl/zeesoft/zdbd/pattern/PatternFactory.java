package nl.zeesoft.zdbd.pattern;

import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;

public class PatternFactory {
	public static PatternSequence getFourOnFloorInstrumentPatternSequence() {
		PatternSequence seq = new PatternSequence();
		SequenceChord chord = new SequenceChord();
		chord.step = 32;
		chord.interval[1]++;
		seq.chordChanges.add(chord);
		seq.patterns.add(getFourOnFloorInstrumentPattern(0));
		seq.patterns.add(getFourOnFloorInstrumentPattern(1));
		seq.patterns.add(getFourOnFloorInstrumentPattern(2));
		seq.sequence[0] = 0;
		seq.sequence[1] = 1;
		seq.sequence[2] = 0;
		seq.sequence[3] = 2;
		return seq;
	}

	public static InstrumentPattern getFourOnFloorInstrumentPattern(int patternNum) {
		InstrumentPattern pattern = new InstrumentPattern();
		pattern.num = patternNum;
	
		pattern.setKick(0,PatternInstrument.ACCENT);
		pattern.setKick(8,PatternInstrument.ACCENT);
		if (patternNum>0) {
			pattern.setKick(14,PatternInstrument.ON);
		}

		pattern.setSnare(4,PatternInstrument.ACCENT);
		if (patternNum>1) {
			pattern.setSnare(9,PatternInstrument.ON);
		}
		pattern.setSnare(12,PatternInstrument.ACCENT);
		if (patternNum>0) {
			pattern.setSnare(15,PatternInstrument.ON);
		}

		pattern.setHihat(0,false,PatternInstrument.ACCENT);
		pattern.setHihat(1,false,PatternInstrument.ON);
		pattern.setHihat(2,true,PatternInstrument.ACCENT);
		pattern.setHihat(3,false,PatternInstrument.ON);
		pattern.setHihat(4,false,PatternInstrument.ACCENT);
		pattern.setHihat(5,false,PatternInstrument.ON);
		pattern.setHihat(6,true,PatternInstrument.ACCENT);
		pattern.setHihat(7,false,PatternInstrument.ON);
		pattern.setHihat(8,false,PatternInstrument.ACCENT);
		pattern.setHihat(9,false,PatternInstrument.ON);
		pattern.setHihat(10,true,PatternInstrument.ACCENT);
		pattern.setHihat(11,false,PatternInstrument.ON);
		pattern.setHihat(12,false,PatternInstrument.ACCENT);
		pattern.setHihat(13,false,PatternInstrument.ON);
		pattern.setHihat(14,true,PatternInstrument.ACCENT);
		pattern.setHihat(15,false,PatternInstrument.ON);
		
		pattern.setRide(0,PatternInstrument.ACCENT);
		pattern.setRide(4,PatternInstrument.ACCENT);
		pattern.setRide(8,PatternInstrument.ACCENT);
		pattern.setRide(12,PatternInstrument.ACCENT);
		pattern.setRide(14,PatternInstrument.ON);

		if (patternNum==0) {
			pattern.setCrash(0,PatternInstrument.ACCENT);
		}
		if (patternNum>1) {
			pattern.setCrash(12,PatternInstrument.ACCENT);
		}
	
		pattern.setPercussion1(2,PatternInstrument.ON);
		pattern.setPercussion1(3,PatternInstrument.ACCENT);
		pattern.setPercussion1(10,PatternInstrument.ON);
		pattern.setPercussion1(11,PatternInstrument.ACCENT);
		
		pattern.setPercussion2(6,PatternInstrument.ACCENT);
		if (patternNum>0) {
			pattern.setPercussion2(8,PatternInstrument.ACCENT);
		}
		pattern.setPercussion2(14,PatternInstrument.ACCENT);
		if (patternNum>1) {
			pattern.setPercussion2(8,PatternInstrument.ACCENT);
		}
		
		pattern.setBass(2, 1, true);
		pattern.setBass(3, 1, true);
		pattern.setBass(6, 1, true);
		pattern.setBass(7, 1, true);
		pattern.setBass(10, 1, true);
		pattern.setBass(11, 1, true);
		pattern.setBass(14, 2, true);

		int note = 0;
		if (patternNum>1) {
			note = 3;
		}
		for (int s = 0; s < 16; s++) {
			if (s==8 && patternNum>1) {
				note = 1;
			}
			if (s==6 || s==11) {
				pattern.setNote(s, 1, note);				
			} else {
				pattern.setNote(s, 0, note);
			}
		}

		return pattern;
	}
}
