package nl.zeesoft.zmmt.sequencer;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Note;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.synthesizer.MidiNote;

public class CompositionToSequenceConvertor {
	private Messenger			messenger		= null;
	private Composition			composition		= null;
	
	public CompositionToSequenceConvertor(Composition composition) {
		this.composition = composition;
	}
	
	public CompositionToSequenceConvertor(Messenger messenger, Composition composition) {
		this.messenger = messenger;
		this.composition = composition;
	}

	// TODO: Echo

	public Sequence getPatternSequence(int patternNumber) {
		Sequence r = createSequence();
		addPatternToSequence(r,1,patternNumber);
		return r;
	}

	protected int addPatternToSequence(Sequence seq,int startTick,int patternNumber) {
		int nextPatternStartTick = startTick;
		Pattern p = composition.getPattern(patternNumber);
		if (p!=null) {
			int patternSteps = composition.getBarsPerPattern() * composition.getStepsPerBar();
			if (p.getBars()>0) {
				patternSteps = p.getBars() * composition.getStepsPerBar();
			}
			int ticksPerStep = (Composition.RESOLUTION * 4) / composition.getStepsPerBeat();
			for (int t = 1; t<=Composition.TRACKS; t++) {
				Track track = seq.getTracks()[t];
				int currentTick = startTick;
				for (int s = 1; s<=patternSteps; s++) {
					for (Note note: p.getNotes()) {
						if (note.track==t && note.step==s) {
							List<MidiNote> midiNotes = composition.getSynthesizerConfiguration().getMidiNotesForNote(note.instrument,note.note,note.accent,0);
							int endTick = ((note.duration * ticksPerStep) - 1);
							for (MidiNote mn: midiNotes) {
								ShortMessage noteOn = new ShortMessage();
								ShortMessage noteOff = new ShortMessage();
								int velocity = (mn.velocity * note.velocityPercentage) / 100;
								try {
									noteOn.setMessage(ShortMessage.NOTE_ON,mn.channel,mn.midiNote,velocity);
									noteOn.setMessage(ShortMessage.NOTE_OFF,mn.channel,mn.midiNote,0);
								} catch (InvalidMidiDataException e) {
									if (messenger!=null) {
										messenger.error(this,"Invalid MIDI data",e);
									} else {
										e.printStackTrace();
									}
									noteOn = null;
									noteOff = null;
								}
								if (noteOn!=null && noteOff!=null) {
									track.add(new MidiEvent(noteOn,currentTick));
									track.add(new MidiEvent(noteOff,endTick));
								}
							}
						}
					}
					currentTick = currentTick + ticksPerStep;
				}
			}
			nextPatternStartTick = nextPatternStartTick + (patternSteps * ticksPerStep);
		}
		return nextPatternStartTick;
	}
	
	protected Sequence createSequence() {
		Sequence r = null;
		try {
			r = new Sequence(Sequence.PPQ,Composition.RESOLUTION,Composition.TRACKS);
		} catch (InvalidMidiDataException e) {
			if (messenger!=null) {
				messenger.error(this,"Invalid MIDI data",e);
			} else {
				e.printStackTrace();
			}
		}
		return r;
	}
}
