package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.Config;

public class CarnivoreMutator extends AnimalMutator {
	public CarnivoreMutator(Config config,CarnivoreEvolver evolver) {
		super(config,false,evolver);
	}
	
	public CarnivoreMutator(Messenger msgr, WorkerUnion uni) {
		super(msgr,uni,false);
	}
}
