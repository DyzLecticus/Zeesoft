package nl.zeesoft.zmmt.player;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.synthesizer.Instrument;
import nl.zeesoft.zmmt.synthesizer.MidiNote;
import nl.zeesoft.zmmt.synthesizer.MidiNoteDelayed;

public class InstrumentPlayer extends Locker {
	private InstrumentPlayerEchoWorker		echoWorker				= null;
	
	private Synthesizer						synthesizer				= null;
	
	private SortedMap<String,MidiNote>		playingNotes			= new TreeMap<String,MidiNote>();
	
	public InstrumentPlayer(Messenger msgr, WorkerUnion uni) {
		super(msgr);
		echoWorker = new InstrumentPlayerEchoWorker(msgr,uni,this);
	}

	public void start() {
		echoWorker.start();
	}

	public void stop() {
		echoWorker.stop();
	}

	public void setSynthesizer(Synthesizer synth) {
		lockMe(this);
		this.synthesizer = synth;
		unlockMe(this);
	}
	
	public void playInstrumentNotes(List<MidiNote> notes) {
		List<MidiNoteDelayed> delayedNotes = new ArrayList<MidiNoteDelayed>();
		boolean delay = false;
		lockMe(this);
		if (synthesizer!=null) {
			for (MidiNote note: notes) {
				if (note instanceof MidiNoteDelayed) {
					delayedNotes.add((MidiNoteDelayed) note);
				} else {
					if (!playingNotes.containsKey(note.getId())) {
						delay = true;
						playingNotes.put(note.getId(),note);
						synthesizer.getChannels()[note.channel].noteOn(note.midiNote,note.velocity);
					}
				}
			}
		}
		unlockMe(this);
		if (delay && delayedNotes.size()>0) {
			echoWorker.addNotes(true,delayedNotes);
		}
	}

	public void stopInstrumentNotes(List<MidiNote> notes) {
		List<MidiNoteDelayed> delayedNotes = new ArrayList<MidiNoteDelayed>();
		lockMe(this);
		if (synthesizer!=null) {
			for (MidiNote note: notes) {
				if (note instanceof MidiNoteDelayed) {
					delayedNotes.add((MidiNoteDelayed) note);
				} else {
					synthesizer.getChannels()[note.channel].noteOff(note.midiNote);
					playingNotes.remove(note.getId());
				}
			}
		}
		unlockMe(this);
		if (delayedNotes.size()>0) {
			echoWorker.addNotes(false,delayedNotes);
		}
	}

	public void stopInstrumentNotes(String name) {
		stopInstrumentNotes(name,false);
	}

	public void stopInstrumentNotes(String name,boolean force) {
		lockMe(this);
		if (synthesizer!=null) {
			SortedMap<String,MidiNote> playing = new TreeMap<String,MidiNote>(playingNotes);
			for (String id: playing.keySet()) {
				MidiNote note = playingNotes.get(id);
				if (note.instrument.equals(name)) {
					synthesizer.getChannels()[note.channel].noteOff(note.midiNote);
					playingNotes.remove(note.getId());
				}
			}
			if (force) {
				int channel1 = Instrument.getMidiChannelForInstrument(name,0);
				synthesizer.getChannels()[channel1].allNotesOff();
				int channel2 = Instrument.getMidiChannelForInstrument(name,1);
				if (channel2>=0 && channel2!=channel1) {
					synthesizer.getChannels()[channel2].allNotesOff();
				}
				int channel3 = Instrument.getMidiChannelForInstrument(name,2);
				if (channel3>=0 && channel3!=channel2 && channel3!=channel1) {
					synthesizer.getChannels()[channel3].allNotesOff();
				}
			}
		}
		unlockMe(this);
		if (name.equals(Instrument.ECHO)) {
			echoWorker.clearNotes();
		}
	}
}
