package nl.zeesoft.zsd.initialize;

import java.util.Date;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class InitializerWorker extends Worker {
	private Initializer		init	= null;
	private InitializeClass cls		= null;
	private Date			started	= null;
	
	public InitializerWorker(Messenger msgr, WorkerUnion union,Initializer init, InitializeClass cls) {
		super(msgr, union);
		setSleep(0);
		this.init = init;
		this.cls = cls;
	}

	@Override
	public void start() {
		started = new Date();
		super.start();
	}
	
	@Override
	public void whileWorking() {
		ZStringBuilder data = null;
		if (cls.fileName.length()>0) {
			data = new ZStringBuilder();
			data.fromFile(cls.fileName);
		}
		cls.obj.initialize(data);
		cls.ms = (new Date()).getTime() - started.getTime();
		init.initializedClass(cls.name);
		stop();
	}
}
