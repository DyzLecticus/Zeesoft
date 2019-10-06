package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.thread.Locker;

public abstract class StreamListenerObject extends Locker implements StreamListener {
	protected Stream	stream	= null;
	
	public StreamListenerObject(Stream stream) {
		super(stream.getMessenger());
		this.stream = stream;
	}
}
