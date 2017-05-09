package nl.zeesoft.zmmt.gui.state;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;

public class CompositionStateManager extends Locker {
	private List<CompositionChangeSubscriber>	subscribers					= new ArrayList<CompositionChangeSubscriber>();
	
	private Composition							composition					= null;
	
	private List<CompositionChangePublisher>	waitingPublishers			= new ArrayList<CompositionChangePublisher>();
	private CompositionPublishWorker 			publishWorker				= null;
	
	public CompositionStateManager(Messenger msgr,WorkerUnion union) {
		super(msgr);
		publishWorker = new CompositionPublishWorker(msgr,union,this);
	}
	
	public void start() {
		lockMe(this);
		waitingPublishers.clear();
		publishWorker.start();
		unlockMe(this);
	}
	
	public void stop() {
		lockMe(this);
		publishWorker.stop();
		unlockMe(this);
	}
	
	public void addSubscriber(CompositionChangeSubscriber subscriber) {
		lockMe(this);
		if (!subscribers.contains(subscriber)) {
			subscribers.add(subscriber);
		}
		unlockMe(this);
	}
	
	public void setComposition(Object source,Composition composition) {
		lockMe(this);
		this.composition = composition;
		for (CompositionChangeSubscriber sub: subscribers) {
			sub.changedComposition(source,composition);
		}
		unlockMe(this);
	}
	
	protected void publishChanges() {
		lockMe(this);
		if (waitingPublishers.size() > 0) {
			for (CompositionChangePublisher publisher: waitingPublishers) {
				publisher.setChangesInComposition(composition);
				for (CompositionChangeSubscriber sub: subscribers) {
					sub.changedComposition(publisher,composition);
				}
			}
			waitingPublishers.clear();
		}
		unlockMe(this);
	}

	public void addWaitingPublisher(CompositionChangePublisher publisher) {
		lockMe(this);
		if (!waitingPublishers.contains(publisher)) {
			waitingPublishers.add(publisher);
		}
		unlockMe(this);
	}
}
