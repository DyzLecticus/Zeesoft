package nl.zeesoft.zodb.event;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Messenger;

public class EvtEventPublisher {
	private List<EvtEventSubscriber> subscribers = new ArrayList<EvtEventSubscriber>();
	
	public void addSubscriber(EvtEventSubscriber l) {
		if (l!=null) {
			List<EvtEventSubscriber> subs = new ArrayList<EvtEventSubscriber>(subscribers);
			if (!subs.contains(l)) {
				subscribers.add(l);
			} else {
				Messenger.getInstance().error(this, "Listener already registered: " + l);
			}
		}
	}
	
	protected void publishEvent(EvtEvent e) {
		// Copy subscribers because new subscribers may be added by events handlers
		List<EvtEventSubscriber> subs = new ArrayList<EvtEventSubscriber>(subscribers);
		for (EvtEventSubscriber l: subs) {
			l.handleEvent(e);
		}
	}
}
