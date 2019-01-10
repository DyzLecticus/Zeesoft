package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class ModuleSequenceWorker extends Worker {
	private KnowledgeBase	kb			= null;
	private Module			from		= null;
	private Module			to			= null;
	private boolean			forward		= true;
	private int				distance	= 0;
	
	public ModuleSequenceWorker(Messenger msgr, WorkerUnion union,KnowledgeBase kb,Module from,Module to,boolean forward, int distance) {
		super(msgr, union);
		this.kb = kb;
		this.from = from;
		this.to = to;
		this.forward = forward;
		this.distance = distance;
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		// TODO Auto-generated method stub
		
	}
	
}
