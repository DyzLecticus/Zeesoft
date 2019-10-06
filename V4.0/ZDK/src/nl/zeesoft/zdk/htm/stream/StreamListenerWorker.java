package nl.zeesoft.zdk.htm.stream;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class StreamListenerWorker extends Worker {
	private Stream					stream			= null;
	private StreamListener			listener		= null;
	
	private Queue<StreamResult>		streamResults	= new LinkedList<StreamResult>();
	
	protected StreamListenerWorker(Messenger msgr, WorkerUnion union,Stream stream,StreamListener listener) {
		super(msgr, union);
		this.stream = stream;
		this.listener = listener;
		setSleep(10);
		setStopOnException(false);
	}
	
	protected void addResults(List<StreamResult> results) {
		lockMe(this);
		for (StreamResult result: results) {
			streamResults.add(result);
		}
		unlockMe(this);
	}
	
	@Override
	protected void whileWorking() {
		flushOutput();
	}
	
	protected void destroy() {
		lockMe(this);
		stream = null;
		listener = null;
		streamResults.clear();
		unlockMe(this);
	}
	
	protected void flushOutput() {
		lockMe(this);
		Queue<StreamResult>	results	= new LinkedList<StreamResult>(streamResults);
		streamResults.clear();
		unlockMe(this);
		for (StreamResult result: results) {
			listener.processedResult(stream, result);
		}
	}
}
