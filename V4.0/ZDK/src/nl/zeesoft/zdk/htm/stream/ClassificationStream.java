package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ClassificationStream extends Stream {
	public ClassificationStream(Pooler pooler,Memory memory,Classifier classifier) {
		super(null,null);
		initialize(pooler,memory,classifier);
	}
	
	public ClassificationStream(StreamEncoder encoder,Pooler pooler,Memory memory,Classifier classifier) {
		super(null,null,encoder);
		initialize(pooler,memory,classifier);
	}
	
	public ClassificationStream(Messenger msgr, WorkerUnion uni,Pooler pooler,Memory memory,Classifier classifier) {
		super(msgr, uni);
		initialize(pooler,memory,classifier);
	}
	
	public ClassificationStream(Messenger msgr, WorkerUnion uni,StreamEncoder encoder,Pooler pooler,Memory memory,Classifier classifier) {
		super(msgr, uni, encoder);
		initialize(pooler,memory,classifier);
	}
	
	protected void initialize(Pooler pooler,Memory memory,Classifier classifier) {
		addInputProcessor(pooler);
		addNextProcessor(memory,0);
		addNextProcessor(classifier,1);
	}
}
