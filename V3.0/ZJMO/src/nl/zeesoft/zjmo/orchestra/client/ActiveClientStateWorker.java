package nl.zeesoft.zjmo.orchestra.client;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;

public class ActiveClientStateWorker extends Worker {
	private ActiveClient	activeClient	= null;
	private MemberClient	stateClient		= null;
	
	protected ActiveClientStateWorker(Messenger msgr, WorkerUnion union, ActiveClient activeClient, MemberClient stateClient) {
		super(msgr, union);
		setSleep(1000);
		this.activeClient = activeClient;
		this.stateClient = stateClient;
	}

	@Override
	public void stop() {
		super.stop();
		stateClient.sendCloseSessionCommand();
		stateClient.close();
	}

	@Override
	public void whileWorking() {
		stateClient.readInput(0);
		stop();
		activeClient.disconnect();
	}
}
