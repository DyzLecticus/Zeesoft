package nl.zeesoft.zodb.event;

public interface EvtEventExceptionSubscriber extends EvtEventSubscriber{
	public void handleEventException(EvtEvent evt, Exception ex);
}
