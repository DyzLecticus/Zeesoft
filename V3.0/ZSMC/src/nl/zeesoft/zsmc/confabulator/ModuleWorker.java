package nl.zeesoft.zsmc.confabulator;

import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ModuleWorker extends Worker {
	private int		confMs		= 0;
	private Date	started		= null;
	private Module	module		= null;
	private boolean	done		= true;

	public ModuleWorker(Messenger msgr, WorkerUnion union,Module mod) {
		super(msgr, union);
		this.module = mod;
		setSleep(0);
	}

	protected void startConfabulation(int confMs) {
		this.confMs = confMs;
		started	= new Date();
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
			try {
				if (module.confabulate()) {
					stop();
					lockMe(this);
					done = true;
					unlockMe(this);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
