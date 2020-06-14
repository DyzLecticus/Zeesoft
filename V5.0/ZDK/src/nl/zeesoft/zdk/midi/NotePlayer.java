package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;

public class NotePlayer {
	private Lock			lock			= new Lock();
	private PatchConfig		instConfig		= null;
	private Synth			synth			= null;
	
	private	int				beatsPerMinute	= 120;
	private int				stepsPerBeat	= 4;
	
	protected NotePlayer(Logger logger, PatchConfig instConfig, Synth synth) {
		this.instConfig = instConfig;
		this.synth = synth;
		lock.setLogger(this, logger);
	}

	public int getBeatsPerMinute() {
		lock.lock(this);
		int r = beatsPerMinute;
		lock.unlock(this);
		return r;
	}

	public void setBeatsPerMinute(int beatsPerMinute) {
		lock.lock(this);
		this.beatsPerMinute = beatsPerMinute;
		lock.unlock(this);
	}

	public int getStepsPerBeat() {
		lock.lock(this);
		int r = stepsPerBeat;
		lock.unlock(this);
		return r;
	}

	public void setStepsPerBeat(int stepsPerBeat) {
		lock.lock(this);
		this.stepsPerBeat = stepsPerBeat;
		lock.unlock(this);
	}

	public void startNotes(int channel, String... notes) {
		List<MidiNote> mns = instConfig.getNotes(channel, notes);
		synth.startNotes(mns);
	}

	public void stopNotes(int channel, String... notes) {
		List<MidiNote> mns = instConfig.getNotes(channel, notes);
		synth.stopNotes(mns);
	}

	public void startNotes(String groupName, String... notes) {
		List<MidiNote> mns = instConfig.getNotes(groupName, notes);
		lock.lock(this);
		synth.startGroupNotes(mns,beatsPerMinute,stepsPerBeat);
		lock.unlock(this);
	}

	public void stopNotes(String groupName, String... notes) {
		List<MidiNote> mns = instConfig.getNotes(groupName, notes);
		lock.lock(this);
		synth.stopGroupNotes(mns,beatsPerMinute,stepsPerBeat);
		lock.unlock(this);
	}
	
	public List<CodeRunner> getDelayedPlayers() {
		List<CodeRunner> r = new ArrayList<CodeRunner>();
		for (DelayedNotePlayer player: synth.getDelayedPlayers()) {
			r.add(player);
		}
		return r;
	}
}
