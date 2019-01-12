package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class Confabulator extends Locker {
	private WorkerUnion						union				= null;
	private KnowledgeBase					kb					= null;
	private String							name				= "";

	public Confabulator(Messenger msgr,WorkerUnion uni,KnowledgeBase kb,String name) {
		super(msgr);
		union = uni;
		this.kb = kb;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void confabulate(ConfabulationObject confab) {
		confab.initialize(getMessenger(), union, kb);
		confab.addLogLine("Confabulating ...");
		confab.confabulate();
		while(confab.isConfabulating()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				if (getMessenger()!=null) {
					getMessenger().error(this,"Confabulation was interrupted",e);
				} else {
					e.printStackTrace();
				}
			}
		}
		confab.logModuleStateNoLock("Confabulated");
	}
}
