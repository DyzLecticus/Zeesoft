package nl.zeesoft.zsd.dialog;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTesterInitializer;

public class DialogHandlerTesterInitializer extends SequenceInterpreterTesterInitializer {
	public DialogHandlerTesterInitializer(DialogHandlerConfiguration configuration) {
		super(configuration);
	}
	
	public DialogHandlerTesterInitializer(Messenger msgr, WorkerUnion uni,DialogHandlerConfiguration configuration) {
		super(msgr,uni,configuration);
	}

	@Override
	protected SequenceInterpreterTester getNewTester(DialogHandlerConfiguration configuration) {
		return new DialogHandlerTester(configuration);
	}
}
