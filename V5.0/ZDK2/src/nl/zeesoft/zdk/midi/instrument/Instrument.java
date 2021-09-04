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
import nl.zeesoft.zdk.midi.pattern.PatternGenerators;
import nl.zeesoft.zdk.midi.pattern.PatternStep;

public abstract class Instrument {
	public static String 				BASS				= "Bass";
	public static String 				STAB				= "Stab";
	public static String 				ARP					= "Arpeggiator";
	public static String 				DRUM				= "Drum";
	
	public String						name				= "";
	public List<InstrumentChannelSound>	sounds				= new ArrayList<InstrumentChannelSound>();
	public List<PatternGenerators>		patternVariations	= new ArrayList<PatternGenerators>();
	public ChordPattern					chordPattern		= null;
	
	public Instrument(String name) {
		this.name = name;
		patternVariations.add(new PatternGenerators());
	}
	
	public abstract List<Integer> getChannels();
	
	public InstrumentChannelSound getSound(int channel, String name) {
		InstrumentChannelSound r = null;
		for (InstrumentChannelSound sound: sounds) {
			if (sound.channel == channel && sound.name.equals(name)) {
				r = sound;
				break;
			}
		}
		return r;
	}
	
	public List<String> getSoundNames() {
		List<String> r = new ArrayList<String>();
		for (InstrumentChannelSound sound: sounds) {
			if (!r.contains(sound.name)) {
				r.add(sound.name);
			}
		}
		return r;
	}

	public PatternGenerator addGenerator(int variation, String soundName) {
		PatternGenerator r = null;
		if (getSoundNames().contains(soundName)) {
			r = patternVariations.get(variation).addGenerator(soundName);
		}
		return r;
	}
	
	public InstrumentPattern generatePattern(int variation) {
		return generatePattern(variation, 0, MidiSys.groove.getTotalSteps());
	}
	
	public InstrumentPattern generatePattern(int variation, int start, int end) {
		return patternVariations.get(variation).generatePattern(name, chordPattern, start, end);
	}
	
	public Sequence generateSequence(InstrumentPattern pattern) {
		Sequence r = MidiSequenceUtil.createSequence(getChannels().size());
		long ticksPerStep = MidiSequenceUtil.getTicksPerStep();
		for (Pattern p: pattern.patterns) {
			long seqEndTick = MidiSequenceUtil.getStepTick(p.stepEnd);
			for (PatternStep ps: p.steps) {
				ChordPatternStep cps = pattern.chordPattern.getStep(ps.step);
				int track = 0;
				for (Integer channel: getChannels()) {
					generateChannelPatternStep(r, track, channel, p, ps, cps, seqEndTick, ticksPerStep);
					track++;
					break;
				}
			}
		}
		return r;
	}
	
	protected void generateChannelPatternStep(
		Sequence seq, int track, int channel, Pattern p, PatternStep ps, ChordPatternStep cps, long seqEndTick, long ticksPerStep
		) {
		InstrumentChannelNote note = getSound(channel, p.name).getNote(ps.accent, cps);		
		long nextActiveTick = seqEndTick;
		PatternStep nps = p.getNextStep(ps.step);
		if (nps!=null) {
			nextActiveTick = MidiSequenceUtil.getStepTick(nps.step);;
		}
		
		long startTick = MidiSequenceUtil.getStepTick(ps.step);
		if (startTick<(nextActiveTick - 2)) {
			generateChannelPatternStepNote(seq, track, note, startTick, nextActiveTick, ticksPerStep);
		}
	}
	
	protected void generateChannelPatternStepNote(
		Sequence seq, int track, InstrumentChannelNote note, long startTick, long nextActiveTick, long ticksPerStep
		) {
		MidiSequenceUtil.createEventOnTrack(
			seq.getTracks()[track],ShortMessage.NOTE_ON,note.channel,note.midiNote,note.velocity,startTick
		);
		long add = (long)(note.hold * (float)ticksPerStep);
		long endTick = startTick + add;
		if (endTick>=nextActiveTick) {
			endTick = nextActiveTick - 1;
		}
		MidiSequenceUtil.createEventOnTrack(
			seq.getTracks()[track],ShortMessage.NOTE_OFF,note.channel,note.midiNote,note.velocity,endTick
		);
	}
}
