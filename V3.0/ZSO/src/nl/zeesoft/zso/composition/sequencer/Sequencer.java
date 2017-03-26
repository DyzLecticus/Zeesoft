package nl.zeesoft.zso.composition.sequencer;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zso.composition.Composition;

public class Sequencer extends Locker {
	public static final String	PLAY_COMPOSITION	= "PLAY_COMPOSITION";
	
	private WorkerUnion			union				= null;
	private	CompositionPlayer	compositionPlayer	= null;

	private Orchestra			orchestra			= null;
	private Composition			composition			= null;
	
	public Sequencer(Orchestra orch,Composition comp) {
		super(new Messenger(null));
		this.orchestra = orch;
		this.composition = comp;
		union = new WorkerUnion(getMessenger());
		compositionPlayer = new CompositionPlayer(getMessenger(),union,this);
	}
	
	public Orchestra getOrchestra() {
		return orchestra;
	}
	
	public Composition getComposition() {
		Composition r = null;
		lockMe(this);
		r = composition;
		unlockMe(this);
		return r;
	}

	public void setComposition(Composition composition) {
		lockMe(this);
		this.composition = composition;
		unlockMe(this);
	}

	public void start() {
		getMessenger().start();
		compositionPlayer.start();
	}

	public void stop(Worker ignoreWorker) {
		if (compositionPlayer.isWorking()) {
			compositionPlayer.stop();
		}
		getMessenger().stop();
		union.stopWorkers(ignoreWorker);
		getMessenger().whileWorking();
	}
}
