package nl.zeesoft.zsd.initialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		List<ZStringBuilder> data = null;
		if (cls.fileNames.size()>0) {
			for (String fileName: cls.fileNames) {
				ZStringBuilder content = new ZStringBuilder();
				String err = content.fromFile(fileName);
				if (err.length()>0) {
					if (cls.errors.length()>0) {
						cls.errors.append("\n");
					}
					cls.errors.append(err);
				} else {
					if (data==null) {
						data = new ArrayList<ZStringBuilder>();
					}
					data.add(content);
				}
			}
		}
		cls.obj.initialize(data);
		cls.ms = (new Date()).getTime() - started.getTime();
		init.initializedClass(cls.name);
		stop();
	}
}
