package nl.zeesoft.zso.orchestra.members;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zso.composition.Composition;

public class SampleConductor extends Conductor {
	private	CompositionPlayer	compositionPlayer	= null;
	private Composition			composition			= null;

	public SampleConductor(Messenger msgr, Orchestra orchestra) {
		super(msgr, orchestra);
		compositionPlayer = new CompositionPlayer(msgr,getUnion(),this);
		JsFile json = new JsFile();
		String err = json.fromFile("composition.json");
		if (err.length()==0) {
			Composition comp = new Composition();
			comp.fromJson(json);
			setComposition(comp);
		} else {
			getMessenger().error(this,"Failed to load composition file: " + err);
		}
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

	@Override
	public boolean start() {
		boolean started = super.start();
		if (started) {
			compositionPlayer.start();
		}
		return started;
	}

	@Override
	public void stop(Worker ignoreWorker) {
		if (compositionPlayer.isWorking()) {
			compositionPlayer.stop();
		}
		super.stop(ignoreWorker);
	}
}
