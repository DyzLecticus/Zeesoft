package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;

public class NotePlayer {
	private Lock			lock			= new Lock();
	private StateManager	stateManager	= null;
	private PatchConfig		patchConfig		= null;
	private Synth			synth			= null;
		
	protected NotePlayer(Logger logger, StateManager stateManager, PatchConfig patchConfig, Synth synth) {
		this.stateManager = stateManager;
		this.patchConfig = patchConfig;
		this.synth = synth;
		lock.setLogger(this, logger);
	}

	public void startNotes(int channel, String... notes) {
		List<MidiNote> mns = patchConfig.getNotes(channel, notes);
		synth.startNotes(mns);
	}

	public void stopNotes(int channel, String... notes) {
		List<MidiNote> mns = patchConfig.getNotes(channel, notes);
		synth.stopNotes(mns);
	}

	public void startNotes(String patchName, String... notes) {
		List<MidiNote> mns = patchConfig.getNotes(patchName, notes);
		lock.lock(this);
		synth.startGroupNotes(mns,stateManager.getMsPerStep());
		lock.unlock(this);
	}

	public void stopNotes(String patchName, String... notes) {
		List<MidiNote> mns = patchConfig.getNotes(patchName, notes);
		lock.lock(this);
		synth.stopGroupNotes(mns,stateManager.getMsPerStep());
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
