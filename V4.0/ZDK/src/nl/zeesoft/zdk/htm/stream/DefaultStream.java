package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class DefaultStream extends Stream {
	public DefaultStream(Pooler pooler,Memory memory) {
		super(null,null);
		initialize(pooler,memory);
	}
	
	public DefaultStream(StreamEncoder encoder,Pooler pooler,Memory memory) {
		super(null,null,encoder);
		initialize(pooler,memory);
	}
	
	public DefaultStream(Messenger msgr, WorkerUnion uni,Pooler pooler,Memory memory) {
		super(msgr, uni);
		initialize(pooler,memory);
	}
	
	public DefaultStream(Messenger msgr, WorkerUnion uni,StreamEncoder encoder,Pooler pooler,Memory memory) {
		super(msgr, uni, encoder);
		initialize(pooler,memory);
	}
	
	public AnomalyDetector getNewAnomalyDetector() {
		AnomalyDetector r = new AnomalyDetector(this);
		addListener(r);
		return r;
	}
	
	protected void initialize(Pooler pooler,Memory memory) {
		addInputProcessor(pooler);
		addNextProcessor(memory,0);
	}
}
