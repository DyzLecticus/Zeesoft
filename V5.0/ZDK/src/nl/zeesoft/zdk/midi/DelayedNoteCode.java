package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.thread.RunCode;

public class DelayedNoteCode extends RunCode {
	protected Synth				synth		= null;
	protected long				actionTime	= 0;
	protected boolean			actionStart = true;
	protected List<MidiNote> 	notes		= new ArrayList<MidiNote>();
	
	protected DelayedNoteCode(Synth synth) {
		this.synth = synth;
	}
	
	@Override
	protected boolean run() {
		boolean done = false;
		if (System.currentTimeMillis()>=actionTime) {
			if (actionStart) {
				synth.startNotes(notes);
			} else {
				synth.stopNotes(notes);
			}
			done = true;
		}
		return done;
	}
	
	
}
