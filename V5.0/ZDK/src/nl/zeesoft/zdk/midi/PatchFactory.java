package nl.zeesoft.zdk.midi;

public class PatchFactory {
	public static DrumPatch getDefaultDrumPatch(String name) {
		DrumPatch r = new DrumPatch(name);
		Pattern pattern = new Pattern();
		pattern.lanes = 4;
		for (int s = 0; s < pattern.steps; s++) {
			if (s==0 || s==8) {
				addPatternNote(pattern, s, 0, "C-3");
			} else if (s==4 || s==12) {
				addPatternNote(pattern, s, 1, "C#3");
			}
			if (s % 4 == 2) {
				addPatternNote(pattern, s, 3, "D#3");
			} else {
				addPatternNote(pattern, s, 2, "D-3");
			}
		}
		r.patterns[0] = pattern;
		
		pattern = pattern.copy();
		addPatternNote(pattern, 11, 0, "C-3");
		addPatternNote(pattern, 14, 0, "C-3");
		r.patterns[1] = pattern;
		return r;
	}
	
	public static Patch getEchoSaw(String name, int delaySteps) {
		return getEchoInstrument(name, delaySteps, 81);
	}
	
	public static Patch getEchoInstrument(String name, int delaySteps, int instrument) {
		Patch r = new Patch(name);
		
		int delay = 0;
		
		Inst inst1 = new Inst();
		inst1.instrument = instrument;
		
		delay += delaySteps;
		Inst inst2 = new Inst();
		inst2.instrument = instrument;
		inst2.velocity = 60;
		inst2.filter = 48;
		inst2.pan = 24;
		inst2.reverb = 24;
		inst2.patchDelaySteps = delay;
		
		delay += delaySteps;
		Inst inst3 = new Inst();
		inst3.instrument = instrument;
		inst3.velocity = 40;
		inst3.filter = 40;
		inst3.pan = 104;
		inst3.reverb = 36;
		inst3.patchDelaySteps = delay;
		
		delay += delaySteps;
		Inst inst4 = new Inst();
		inst3.instrument = instrument;
		inst4.velocity = 20;
		inst4.filter = 32;
		inst4.reverb = 48;
		inst4.patchDelaySteps = delay;
		
		inst1.setLfoSource(Inst.FILTER,0,0.3F,true);
		inst2.setLfoSource(Inst.FILTER,0,0.25F,true);
		inst3.setLfoSource(Inst.FILTER,0,0.20F,true);
		inst4.setLfoSource(Inst.FILTER,0,0.15F,true);
		
		r.instruments.add(inst1);
		r.instruments.add(inst2);
		r.instruments.add(inst3);
		r.instruments.add(inst4);
		
		Pattern pattern = new Pattern();
		pattern.lanes = 3;
		addPatternNote(pattern, 0, 0, "F-3", 4);
		addPatternNote(pattern, 0, 1, "A-3", 4);
		addPatternNote(pattern, 0, 2, "C-4", 4);
		r.patterns[0] = pattern;
		return r;
	}
	
	private static void addPatternNote(Pattern pattern, int step, int lane, String note) {
		addPatternNote(pattern, step, lane, note, 1);
	}
	
	private static void addPatternNote(Pattern pattern, int step, int lane, String note, int duration) {
		PatternNote pn = new PatternNote();
		pn.fromString(note);
		pn.step = step;
		pn.lane = lane;
		pn.duration = duration;
		pattern.notes.add(pn);
	}
}
