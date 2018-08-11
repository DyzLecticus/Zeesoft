package nl.zeesoft.zsds;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class DataGenerator extends Worker {
	private AppStateManager		manager		= null;
	private boolean				load		= false;
	private boolean				reload		= false;
	
	public DataGenerator(Messenger msgr, WorkerUnion union,AppStateManager manager) {
		super(msgr, union);
		this.manager = manager;
		setSleep(1);
		setStopOnException(true);
	}

	public void generate(boolean load,boolean reload) {
		this.load = load;
		this.reload = reload;
		start();
	}

	@Override
	public void whileWorking() {
		boolean generated = manager.generate();
		if (generated && load) {
			if (reload) {
				manager.reload();
			} else {
				manager.load();
			}
		}
		stop();
	}
	
	
}
