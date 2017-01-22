package nl.zeesoft.zodb.event;

import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;

public class EvtEventPublisher {
	private EvtEventSubscriberList subscribers = new EvtEventSubscriberList();

	public void addSubscriber(EvtEventSubscriber sub) {
		subscribers.addSubscriber(sub);
	}

	public void insertSubscriber(EvtEventSubscriber sub) {
		subscribers.insertSubscriber(sub);
	}
	
	protected void publishEvent(EvtEvent evt) {
		List<EvtEventSubscriber> subs = subscribers.getSubscribers();
		for (EvtEventSubscriber sub: subs) {
			try {
				sub.handleEvent(evt);
			} catch (Exception ex) {
				publishEventSubscriberException(evt,sub,ex);
			}
		}
	}

	protected void publishEventSubscriberException(EvtEvent evt, EvtEventSubscriber sub, Exception ex) {
		Messenger.getInstance().error(evt.getSource(),"Error while publishing " + evt.getType() + " event: " + ex + "\n" + Generic.getCallStackString(ex.getStackTrace(),""));
		if (sub instanceof EvtEventExceptionSubscriber) {
			((EvtEventExceptionSubscriber) sub).handleEventException(evt, ex);
		} else {
			subscribers.removeSubscriber(sub);
		}
	}
}
