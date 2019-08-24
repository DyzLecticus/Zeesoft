package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.Config;

public class CarnivoreEvolver extends AnimalEvolver {
	public CarnivoreEvolver(Config config) {
		super(config,false);
	}
	
	public CarnivoreEvolver(Messenger msgr, WorkerUnion uni) {
		super(msgr,uni,false);
	}
}
