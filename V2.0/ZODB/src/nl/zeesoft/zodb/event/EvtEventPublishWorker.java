package nl.zeesoft.zodb.event;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public class EvtEventPublishWorker extends Worker {
	private EvtEventSubscriberList 	subscribers 	= new EvtEventSubscriberList();
	private boolean					publishOnStop 	= false;
	private List<EvtEvent>			events			= new ArrayList<EvtEvent>();

	public void addSubscriber(EvtEventSubscriber sub) {
		subscribers.addSubscriber(sub);
	}

	public void insertSubscriber(EvtEventSubscriber sub) {
		subscribers.insertSubscriber(sub);
	}
	
	protected void publishEvent(EvtEvent e) {
		lockMe(this);
		// Cleanup if not started or no subscribers
		if (events.size()>1000 && (subscribers.getSize()==0 || !isWorking())) {
			events.clear();
		}
		events.add(e);
		unlockMe(this);
	}

	@Override
	public void stop() {
		super.stop();
		if (publishOnStop) {
			while(isWorking()) {
				sleep(10);
			}
			publishEvents();
		}
	}

	@Override
	public void whileWorking() {
		publishEvents();
	}
	
	protected void publishEvents() {
		lockMe(this);
		List<EvtEventSubscriber> subs = null;
		List<EvtEvent> evts = null;
		// Wait with publishing events until at least one subscriber has been added
		if (subscribers.getSize()>0) {
			evts = new ArrayList<EvtEvent>(events);
			subs = subscribers.getSubscribers();
			events.clear();
		}
		unlockMe(this);
		if (subs!=null) {
			for (EvtEvent evt: evts) {
				publishingEvent(evt);
				for (EvtEventSubscriber sub: subs) {
					try {
						sub.handleEvent(evt);
					} catch (Exception ex) {
						publishEventSubscriberException(evt,sub,ex);
					}
				}
			}
		}
	}

	// Override to handle events internally
	protected void publishingEvent(EvtEvent evt) {
		
	}

	protected void publishEventSubscriberException(EvtEvent evt, EvtEventSubscriber sub, Exception ex) {
		Messenger.getInstance().error(evt.getSource(),"Error while publishing " + evt.getType() + " event: " + ex + "\n" + Generic.getCallStackString(ex.getStackTrace(),""));
		if (sub instanceof EvtEventExceptionSubscriber) {
			((EvtEventExceptionSubscriber) sub).handleEventException(evt, ex);
		} else {
			subscribers.removeSubscriber(sub);
		}
	}
	
	/**
	 * @param publishOnStop the publishOnStop to set
	 */
	protected void setPublishOnStop(boolean publishOnStop) {
		this.publishOnStop = publishOnStop;
	}
}
