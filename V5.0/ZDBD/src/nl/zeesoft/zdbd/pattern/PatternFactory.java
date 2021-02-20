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
	
	public static PatternSequence getFourOnFloorInstrumentPatternSequence2() {
		PatternSequence seq = new PatternSequence();
		SequenceChord chord = seq.getChordForStep(0,true);
		chord.step = 0;
		chord.baseNote = 2;
		chord.interval[0] = 3;
		chord.interval[1] = 7;
		chord.interval[2] = 10;
		
		chord = chord.copy();
		chord.step = 32;
		chord.baseNote = 4;
		seq.chordChanges.add(chord);

		seq.patterns.add(getFourOnFloorInstrumentPattern2(0));
		seq.patterns.add(getFourOnFloorInstrumentPattern2(1));
		seq.patterns.add(getFourOnFloorInstrumentPattern2(2));
		seq.sequence[0] = 0;
		seq.sequence[1] = 1;
		seq.sequence[2] = 0;
		seq.sequence[3] = 2;
		return seq;
	}
	
	public static PatternSequence getFourOnFloorInstrumentPatternSequence3() {
		PatternSequence seq = new PatternSequence();
		SequenceChord chord = seq.getChordForStep(0,true);
		chord.step = 0;
		chord.baseNote = 2;
		chord.interval[0] = 3;
		chord.interval[1] = 8;
		chord.interval[2] = 10;
		
		chord = chord.copy();
		chord.step = 32;
		chord.interval[1] = 7;
		seq.chordChanges.add(chord);
		
		seq.patterns.add(getFourOnFloorInstrumentPattern3(0));
		seq.patterns.add(getFourOnFloorInstrumentPattern3(1));
		seq.patterns.add(getFourOnFloorInstrumentPattern3(2));
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

		pattern.setStab(2, 1, false);
		pattern.setStab(4, 1, true);
		pattern.setStab(7, 1, true);
		if (patternNum>0) {
			pattern.setStab(10, 1, false);
			pattern.setStab(12, 3, true);
		}

		return pattern;
	}

	public static InstrumentPattern getFourOnFloorInstrumentPattern2(int patternNum) {
		InstrumentPattern pattern = new InstrumentPattern();
		pattern.num = patternNum;
	
		pattern.setKick(0,PatternInstrument.ACCENT);
		pattern.setKick(4,PatternInstrument.ACCENT);
		pattern.setKick(8,PatternInstrument.ACCENT);
		pattern.setKick(12,PatternInstrument.ACCENT);
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
		
		pattern.setPercussion2(4,PatternInstrument.ACCENT);
		pattern.setPercussion2(12,PatternInstrument.ACCENT);
		if (patternNum>0) {
			pattern.setPercussion2(14,PatternInstrument.ACCENT);
		}
		
		pattern.setBass(2, 2, true);
		pattern.setBass(6, 2, true);
		pattern.setBass(10, 2, true);
		pattern.setBass(14, 2, true);

		if (patternNum>0) {
			int note = 0;
			int octave = 1;
			for (int s = 0; s < 16; s++) {
				if (s==4) {
					octave = 0;
					note = 3;
				}
				if (s==8) {
					note = 2;
				}
				if (s==12) {
					note = 1;
				}
				pattern.setNote(s, octave, note);
			}
		}
		
		pattern.setStab(2, 1, false);
		pattern.setStab(5, 1, false);
		pattern.setStab(8, 3, true);
		if (patternNum>0) {
			pattern.setStab(12, 3, true);
		}

		return pattern;
	}

	public static InstrumentPattern getFourOnFloorInstrumentPattern3(int patternNum) {
		InstrumentPattern pattern = new InstrumentPattern();
		pattern.num = patternNum;
	
		pattern.setKick(0,PatternInstrument.ACCENT);
		pattern.setKick(4,PatternInstrument.ACCENT);
		pattern.setKick(8,PatternInstrument.ACCENT);
		pattern.setKick(12,PatternInstrument.ACCENT);
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
	
		pattern.setPercussion1(0,PatternInstrument.ACCENT);
		pattern.setPercussion1(3,PatternInstrument.ON);
		pattern.setPercussion1(6,PatternInstrument.ACCENT);
		pattern.setPercussion1(9,PatternInstrument.ON);
		pattern.setPercussion1(12,PatternInstrument.ACCENT);
		pattern.setPercussion1(15,PatternInstrument.ACCENT);
		
		pattern.setPercussion2(4,PatternInstrument.ACCENT);
		pattern.setPercussion2(12,PatternInstrument.ACCENT);
		if (patternNum>0) {
			pattern.setPercussion2(15,PatternInstrument.ACCENT);
		}
		
		for (int s = 0; s < 16; s++) {
			if (s % 4 >0) {
				boolean accent = s % 4 > 1;
				pattern.setBass(s, 1, accent);
			}
		}
		if (patternNum==0) {
			pattern.setNote(13, 0, 2);
			pattern.setNote(14, 0, 1);
		} else {
			pattern.setNote(13, 0, 3);
			pattern.setNote(14, 0, 2);
			pattern.setNote(15, 0, 1);
		}

		/*
		if (patternNum>0) {
			int note = 0;
			int octave = 1;
			for (int s = 0; s < 16; s++) {
				if (s==4) {
					octave = 0;
					note = 3;
				}
				if (s==8) {
					note = 2;
				}
				if (s==12) {
					note = 1;
				}
				pattern.setNote(s, octave, note);
			}
		}
		*/
		
		if (patternNum==0) {
			pattern.setStab(0, 1, true);
			pattern.setStab(3, 1, false);
			pattern.setStab(6, 1, true);
			pattern.setStab(9, 1, false);
			pattern.setStab(12, 2, true);
			pattern.setStab(15, 1, false);
		} else {
			pattern.setStab(2, 1, true);
			pattern.setStab(5, 1, false);
			pattern.setStab(8, 1, true);
			pattern.setStab(11, 1, false);
			pattern.setStab(14, 2, true);
		}

		return pattern;
	}
}
