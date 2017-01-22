package nl.zeesoft.zodb.event;

public interface EvtEventSubscriber {
	public void handleEvent(EvtEvent e);
}
