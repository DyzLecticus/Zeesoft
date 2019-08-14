package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.Config;

public class HerbivoreEvolver extends AnimalEvolver {
	public HerbivoreEvolver(Config config) {
		super(config,true);
	}
	
	public HerbivoreEvolver(Messenger msgr, WorkerUnion uni,boolean herbivore) {
		super(msgr,uni,true);
	}
}
