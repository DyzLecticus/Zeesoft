package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ClassificationStream extends DefaultStream {
	public ClassificationStream(Pooler pooler,Memory memory,Classifier classifier) {
		super(pooler,memory);
		initialize(classifier);
	}
	
	public ClassificationStream(StreamEncoder encoder,Pooler pooler,Memory memory,Classifier classifier) {
		super(encoder,pooler,memory);
		initialize(classifier);
	}
	
	public ClassificationStream(Messenger msgr, WorkerUnion uni,Pooler pooler,Memory memory,Classifier classifier) {
		super(msgr,uni,pooler,memory);
		initialize(classifier);
	}
	
	public ClassificationStream(Messenger msgr, WorkerUnion uni,StreamEncoder encoder,Pooler pooler,Memory memory,Classifier classifier) {
		super(msgr,uni,encoder,pooler,memory);
		initialize(classifier);
	}
	
	protected void initialize(Classifier classifier) {
		addNextProcessor(classifier,1);
	}
}
