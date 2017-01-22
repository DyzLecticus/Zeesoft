package nl.zeesoft.zodb.event;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;

public class EvtEventSubscriberList extends Locker {
	private List<EvtEventSubscriber> subscribers = new ArrayList<EvtEventSubscriber>();

	protected void addSubscriber(EvtEventSubscriber sub) {
		addSubscriber(sub,false);
	}

	protected void insertSubscriber(EvtEventSubscriber sub) {
		addSubscriber(sub,true);
	}
	
	protected List<EvtEventSubscriber> getSubscribers() {
		List<EvtEventSubscriber> subs = null;
		lockMe(this);
		subs = new ArrayList<EvtEventSubscriber>(subscribers);
		unlockMe(this);
		return subs;
	}

	protected void removeSubscriber(EvtEventSubscriber sub) {
		lockMe(this);
		subscribers.remove(sub);
		unlockMe(this);
	}
	
	protected int getSize() {
		int s = 0;
		lockMe(this);
		s = subscribers.size();
		unlockMe(this);
		return s;
	}

	private void addSubscriber(EvtEventSubscriber sub,boolean first) {
		if (sub!=null) {
			lockMe(this);
			if (!subscribers.contains(sub)) {
				if (first) {
					subscribers.add(0,sub);
				} else {
					subscribers.add(sub);
				}
			} else {
				Messenger.getInstance().error(this, "Subscriber already added: " + sub);
			}
			unlockMe(this);
		}
	}
}
