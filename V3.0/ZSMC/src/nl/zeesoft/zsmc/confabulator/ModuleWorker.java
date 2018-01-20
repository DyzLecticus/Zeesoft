package nl.zeesoft.zsmc.confabulator;

import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ModuleWorker extends Worker {
	private int		confMs		= 0;
	private Date	started		= null;
	private Module	module		= null;

	public ModuleWorker(Messenger msgr, WorkerUnion union,Module mod) {
		super(msgr, union);
		this.module = mod;
		setSleep(0);
	}

	protected void startConfabulation(int confMs) {
		this.confMs = confMs;
		started	= new Date();
		super.start();
	}
	
	@Override
	public void whileWorking() {
		Date d = new Date();
		if (d.getTime()>(started.getTime() + confMs)) {
			stop();
		} else {
			module.confabulate();
		}
	}

}
