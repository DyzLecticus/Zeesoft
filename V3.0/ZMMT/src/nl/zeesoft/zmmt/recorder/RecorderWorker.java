package nl.zeesoft.zmmt.recorder;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class RecorderWorker extends Worker {
	private Recorder recorder = null;
	
	public RecorderWorker(Messenger msgr, WorkerUnion uni, Recorder rec) {
		super(msgr, uni);
		recorder = rec;
	}

	@Override
	public void whileWorking() {
		recorder.record();
	}
}
