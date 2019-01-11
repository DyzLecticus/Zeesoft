package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsmc.confab.confabs.ExtensionConfabulation;
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
		if (confab instanceof ExtensionConfabulation) {
			confabulateExtension((ExtensionConfabulation) confab);
		}
	}
	
	protected void confabulateExtension(ExtensionConfabulation confab) {
		int i = 0;
		for (String symbol: confab.symbols) {
			confab.modules.get(i).setActiveSymbol(symbol);
			confab.modules.get(i).setLocked(true);
			i++;
		}
	}
}
