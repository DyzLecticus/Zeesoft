package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class Confabulator extends Locker {
	private WorkerUnion						union				= null;
	private KnowledgeBase					kb					= null;
	private String							name				= "";

	public Confabulator(Messenger msgr,WorkerUnion uni,KnowledgeBase kb,String name,int maxDistance) {
		super(msgr);
		union = uni;
		this.kb = kb;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
