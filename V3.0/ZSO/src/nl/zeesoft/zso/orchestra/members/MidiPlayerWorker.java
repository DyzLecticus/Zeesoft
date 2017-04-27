package nl.zeesoft.zso.orchestra.members;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class MidiPlayerWorker extends Worker {
	private MidiPlayer		player		= null;
	private long			end			= 0;
	
	private String			instrument	= "";
	private List<Integer>	notes		= null;
	
	public MidiPlayerWorker(Messenger msgr, WorkerUnion union,MidiPlayer player) {
		super(msgr,union);
		setSleep(1);
		this.player = player;
	}
	
	public void play(String instrument,List<Integer> notes,int velocity,long durationMs) {
		if (isWorking()) {
			stop();
		}
		this.instrument = instrument;
		this.notes = notes;
		end = (new Date()).getTime() + durationMs;
		start();
		player.playInstrumentNotes(instrument, notes, velocity);
	}
	
	@Override
	public void whileWorking() {
		if ((new Date()).getTime()>=end) {
			stop();
			player.stopInstrumentNotes(instrument, notes);
			player.removeWorker(this);
		}
	}
}
