package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.Config;

public class HerbivoreMutator extends AnimalMutator {
	public HerbivoreMutator(Config config,HerbivoreEvolver evolver) {
		super(config,true,evolver);
	}
	
	public HerbivoreMutator(Messenger msgr, WorkerUnion uni) {
		super(msgr,uni,true);
	}
}
