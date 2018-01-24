package nl.zeesoft.zsmc.confabulator;

import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ModuleWorker extends Worker {
	private int		confMs				= 0;
	private int		contMs				= 0;
	private Date	started				= null;
	private Date	previousContraction	= null;
	private Module	module				= null;
	private boolean	done				= true;

	public ModuleWorker(Messenger msgr, WorkerUnion union,Module mod) {
		super(msgr, union);
		this.module = mod;
		setSleep(0);
	}

	protected void startConfabulation(int confMs,int contMs) {
		this.confMs = confMs;
		this.contMs = contMs;
		started	= new Date();
		previousContraction = new Date();
		lockMe(this);
		done = false;
		unlockMe(this);
		super.start();
	}

	public boolean isDone() {
		boolean r = false;
		lockMe(this);
		r = done;
		unlockMe(this);
		return r;
	}
	
	@Override
	public void whileWorking() {
		Date d = new Date();
		if (d.getTime()>(started.getTime() + confMs)) {
			stop();
			lockMe(this);
			done = true;
			unlockMe(this);
		} else {
			if (d.getTime()>(previousContraction.getTime() + contMs)) {
				previousContraction = d;
				module.contract();
			}
			if (module.confabulate()) {
				stop();
				lockMe(this);
				done = true;
				unlockMe(this);
			}
		}
	}

}
