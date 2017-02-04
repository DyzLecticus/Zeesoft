package nl.zeesoft.zdk;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * Factory for ZDK application singletons.
 */
public class ZDKFactory {
	private Messenger	msgr	= null;
	private WorkerUnion	union	= null;

	public Messenger getMessenger() {
		if (msgr==null) {
			msgr = new Messenger(getWorkerUnion(null));
		}
		return msgr;
	}
	
	public Messenger getMessenger(WorkerUnion union) {
		if (msgr==null) {
			msgr = new Messenger(union);
		}
		return msgr;
	}
	
	public WorkerUnion getWorkerUnion() {
		if (union==null) {
			union = new WorkerUnion(getMessenger(null));
		}
		return union;
	}

	public WorkerUnion getWorkerUnion(Messenger msgr) {
		if (union==null) {
			union = new WorkerUnion(msgr);
		}
		return union;
	}
}
