package nl.zeesoft.zsd.interpret;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.initialize.Initializer;

public class SequenceInterpreterTesterInitializer extends Initializer {
	private DialogHandlerConfiguration			configuration		= null;
	private SequenceInterpreterTester			tester				= null;

	public SequenceInterpreterTesterInitializer(DialogHandlerConfiguration configuration) {
		super(null,null);
		this.configuration = configuration;
		initializeTester();
	}
	
	public SequenceInterpreterTesterInitializer(Messenger msgr, WorkerUnion uni,DialogHandlerConfiguration configuration) {
		super(msgr,uni);
		this.configuration = configuration;
		initializeTester();
	}
	
	public String getFileName() {
		return configuration.getBase().getDataDir() + configuration.getBase().getSelfTestBaseLineFileName();
	}
	
	public SequenceInterpreterTester getTester() {
		return tester;
	}
	
	protected void initializeTester() {
		this.tester = getNewTester(configuration);
		addClass("tester",tester,getFileName());
	}
	
	protected SequenceInterpreterTester getNewTester(DialogHandlerConfiguration configuration) {
		return new SequenceInterpreterTester(configuration);
	}
}