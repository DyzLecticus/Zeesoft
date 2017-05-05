package nl.zeesoft.zmmt.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.syntesizer.MidiNote;
import nl.zeesoft.zmmt.syntesizer.MidiNoteDelayed;

public class InstrumentPlayerEchoWorker extends Worker {
	private InstrumentPlayer		player		= null;

	private List<MidiNoteDelayed>	playNotes	= new ArrayList<MidiNoteDelayed>();		
	private List<MidiNoteDelayed>	stopNotes	= new ArrayList<MidiNoteDelayed>();		

	public InstrumentPlayerEchoWorker(Messenger msgr, WorkerUnion union,InstrumentPlayer player) {
		super(msgr, union);
		this.player = player;
		setSleep(1);
	}

	protected void addNotes(boolean play,List<MidiNoteDelayed> notes) {
		lockMe(this);
		List<MidiNoteDelayed> list = null;
		if (play) {
			list = playNotes;
		} else {
			list = stopNotes;
		}
		for (MidiNoteDelayed note: notes) {
			list.add(note);
		}
		unlockMe(this);
	}

	protected void clearNotes() {
		lockMe(this);
		playNotes.clear();
		stopNotes.clear();
		unlockMe(this);
	}
	
	@Override
	public void whileWorking() {
		long now = (new Date()).getTime(); 
		List<MidiNote> playList = new ArrayList<MidiNote>();
		List<MidiNote> stopList = new ArrayList<MidiNote>();
		lockMe(this);
		List<MidiNoteDelayed> list = new ArrayList<MidiNoteDelayed>(playNotes);
		for (MidiNoteDelayed note: list) {
			if (note.playDateTime<=now) {
				playList.add(note.getNewMidiNote());
				playNotes.remove(note);
			}
		}
		list = new ArrayList<MidiNoteDelayed>(stopNotes);
		for (MidiNoteDelayed note: list) {
			if (note.playDateTime<=now) {
				stopList.add(note.getNewMidiNote());
				stopNotes.remove(note);
			}
		}
		unlockMe(this);
		if (playList.size()>0) {
			player.playInstrumentNotes(playList);
		}
		if (stopList.size()>0) {
			player.stopInstrumentNotes(stopList);
		}
	}
}
