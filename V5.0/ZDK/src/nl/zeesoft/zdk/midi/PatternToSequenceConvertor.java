package nl.zeesoft.zdk.midi;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class PatternToSequenceConvertor {
	public static final int		TEMPO			= 0x51;
	public static final int		TEXT			= 0x01;
	public static final int		RESOLUTION		= 960;
	
	private StateManager		stateManager	= null;
	private SynthManager		synthManager	= null;

	public PatternToSequenceConvertor(StateManager stateManager, SynthManager synthManager) {
		this.stateManager = stateManager;
		this.synthManager = synthManager;
	}
	
	public Sequence generateSequenceForPattern(String patchName, Pattern pattern) {
		Sequence r = null;
		State state = stateManager.getState();
		Patch patch = synthManager.getPatch(patchName);
		if (patch!=null) {
			r = createSequence(patch);
			addTempoMetaEventToSequence(state,r);
			addNotesToSequence(state,r,patch,pattern);
		}
		return r;
	}
	
	protected Sequence createSequence(Patch patch) {
		Sequence r = null;
		try {
			r = new Sequence(Sequence.PPQ,RESOLUTION,patch.instruments.size());
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return r;
	}

	protected void addTempoMetaEventToSequence(State state, Sequence sequence) {
		Track track = sequence.getTracks()[0];
		int tempo = (60000000 / state.beatsPerMinute);
		byte[] b = new byte[3];
		int tmp = tempo >> 16;
		b[0] = (byte) tmp;
		tmp = tempo >> 8;
		b[1] = (byte) tmp;
		b[2] = (byte) tempo;
		createMetaEventOnTrack(track,TEMPO,b,b.length,0);
	}
	
	protected void addNotesToSequence(State state, Sequence sequence, Patch patch, Pattern pattern) {
		long maxTick = getStepTick(state,pattern.steps);
		for (PatternNote pn: pattern.notes) {
			if (pn.duration>0 && pn.step<pattern.steps) {
				int index = 0;
				for (Inst inst: patch.instruments) {
					List<MidiNote> mns = inst.getNotes(pn.toString());
					for (MidiNote mn: mns) {
						long startTick = getStepTick(state,pn.step + mn.delaySteps);
						if (startTick>maxTick) {
							startTick = startTick % maxTick;
						}
						Track track = sequence.getTracks()[index];
						createEventOnTrack(track,ShortMessage.NOTE_ON,inst.channel,mn.getMidiNoteNum(),mn.velocity,startTick);
						long add = (pn.duration - 1) * RESOLUTION;
						if (mn.stepPercentage<1) {
							add += (mn.stepPercentage * (float)RESOLUTION);
						}
						long endTick = startTick + add;
						long nextNoteTick = getStepTick(state,pn.step + pn.duration);
						System.out.println(startTick + " - " + endTick + ": " + mn.getMidiNoteNum());
						if (endTick>nextNoteTick) {
							endTick = nextNoteTick - 1;
						}
						if (endTick>maxTick) {
							endTick = endTick % maxTick;
						}
						createEventOnTrack(track,ShortMessage.NOTE_OFF,inst.channel,mn.getMidiNoteNum(),mn.velocity,endTick);
					}
					index++;
				}
			}
		}
	}
	
	protected long getStepTick(State state,int step) {
		long r = 0;
		if (step>0) {
			r = step * RESOLUTION;
			int beatStep = step % state.stepsPerBeat;
			float delayPercentage = state.getStepDelayPercentages()[beatStep];
			if (delayPercentage>0) {
				r = r + (int)(delayPercentage * (float)RESOLUTION);
			}
		}
		return r;
	}
	
	protected void createEventOnTrack(Track track, int type, int channel, int num, int val, long tick) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(type,channel,num,val); 
			MidiEvent event = new MidiEvent(message,tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	protected void createMetaEventOnTrack(Track track, int type, byte[] data, int length, long tick) {
		MetaMessage message = new MetaMessage();
		try {
			message.setMessage(type,data,length);
			MidiEvent event = new MidiEvent(message,tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
}
