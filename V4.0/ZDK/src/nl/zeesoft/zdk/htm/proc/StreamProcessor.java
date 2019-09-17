package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class StreamProcessor extends Locker {
	private List<Processable>	processors	= new ArrayList<Processable>();
	
	private Queue<SDR>			buffer		= new LinkedList<SDR>();
	
	public StreamProcessor(Messenger msgr) {
		super(msgr);
		// TODO Auto-generated constructor stub
	}
	
	public void addProcessor(Processable processor) {
		lockMe(this);
		processors.add(processor);
		unlockMe(this);
	}
	
	public void addSDR(SDR sdr) {
		lockMe(this);
		buffer.add(sdr);
		unlockMe(this);
	}
	
	protected void processNextSDR() {
		
	}
}
