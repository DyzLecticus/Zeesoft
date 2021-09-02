package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;

import nl.zeesoft.zdk.midi.MidiSequenceUtil;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.pattern.ChordPattern;
import nl.zeesoft.zdk.midi.pattern.ChordPatternStep;
import nl.zeesoft.zdk.midi.pattern.InstrumentPattern;
import nl.zeesoft.zdk.midi.pattern.Pattern;
import nl.zeesoft.zdk.midi.pattern.PatternGenerator;
import nl.zeesoft.zdk.midi.pattern.PatternStep;

public abstract class Instrument {
	public static String 			BASS				= "Bass";
	public static String 			STAB				= "Stab";
	public static String 			ARP					= "Arpeggiator";
	public static String 			DRUM				= "Drum";
	
	public String					name				= "";
	public List<PatternGenerator>	generators			= new ArrayList<PatternGenerator>();
	public ChordPattern				chordPattern		= null;
	
	public int[]					channelBaseOctaves	= {3};
	
	public Instrument(String name) {
		this.name = name;
	}
	
	public abstract List<Integer> getChannels();
	
	public PatternGenerator addGenerator(String name) {
		PatternGenerator r = null;
		if (getGenerator(name)==null) {
			r = new PatternGenerator();
			r.name = name;
			generators.add(r);
		}
		return r;
	}
	
	public PatternGenerator getGenerator(String name) {
		PatternGenerator r = null;
		for (PatternGenerator pg: generators) {
			if (pg.name.equals(name)) {
				r = pg;
			}
		}
		return r;
	}
	
	public InstrumentPattern generatePattern() {
		return generatePattern(0, MidiSys.groove.getTotalSteps());
	}
	
	public InstrumentPattern generatePattern(int start, int end) {
		InstrumentPattern r = new InstrumentPattern();
		r.name = name;
		if (chordPattern==null) {
			r.chordPattern = MidiSys.chordPattern;
		} else {
			r.chordPattern = chordPattern;
		}
		for (PatternGenerator pg: generators) {
			r.patterns.add(pg.generatePattern(start, end));
		}
		return r;
	}
	
	public Sequence generateSequence(InstrumentPattern pattern) {
		Sequence r = MidiSequenceUtil.createSequence(getChannels().size());
		long ticksPerStep = MidiSequenceUtil.getTicksPerStep();
		for (Pattern p: pattern.patterns) {
			PatternGenerator generator = getGenerator(p.name);
			long seqEndTick = MidiSequenceUtil.getStepTick(p.stepEnd);
			for (PatternStep ps: p.steps) {
				
				float hold = generator.hold;
				int velocity = generator.velocity;
				if (ps.accent) {
					hold = generator.accentHold;
					velocity = generator.accentVelocity;
				}
				
				ChordPatternStep cps = pattern.chordPattern.getStep(ps.step);

				int track = 0;
				for (Integer channel: getChannels()) {
					int midiNote = (channelBaseOctaves[track] * 12) + cps.baseNote;
					if (generator.chordNote>0) {
						midiNote += cps.interval[(generator.chordNote - 1)];
					}
					
					long nextActiveTick = seqEndTick;
					// TODO: Get next step to limit next active tick
					
					long startTick = MidiSequenceUtil.getStepTick(ps.step);
					if (startTick<(nextActiveTick - 2)) {
						MidiSequenceUtil.createEventOnTrack(
							r.getTracks()[track],ShortMessage.NOTE_ON,channel,midiNote,velocity,startTick
						);
						long add = (long)(hold * (float)ticksPerStep);
						long endTick = startTick + add;
						if (endTick>=nextActiveTick) {
							endTick = nextActiveTick - 1;
						}
						MidiSequenceUtil.createEventOnTrack(
							r.getTracks()[track],ShortMessage.NOTE_OFF,channel,midiNote,velocity,endTick
						);
					}
					
					track++;
				}
			}
		}
		return r;
	}
}
