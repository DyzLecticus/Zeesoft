package nl.zeesoft.zsda.test;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zdk.htm.grid.ZGridFactory;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsda.grid.PersistableLogger;

public class TestPersistableLogger extends TestObject {
	public TestPersistableLogger(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPersistableLogger(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		// TODO Describe
		/*
		System.out.println("This test shows how to convert a *Config* instance to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the test configuration");
		System.out.println("Config config = new Config();");
		System.out.println("// Convert the test configuration to JSON");
		System.out.println("JsFile json = config.toJson();");
		System.out.println("// Convert the test configuration from JSON");
		System.out.println("config.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(Config.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		// TODO Auto-generated method stub
		ZDKFactory factory = new ZDKFactory();
		Messenger msgr = factory.getMessenger();
		WorkerUnion uni = factory.getWorkerUnion(msgr);
		
		msgr.setPrintDebugMessages(true);
		msgr.start();
		
		ZGridFactory gridFactory = new ZGridFactory(msgr,uni);
		gridFactory.initializeDefaultGrid();
		ZGrid grid = gridFactory.buildNewGrid();
		PersistableLogger logger = new PersistableLogger(msgr,grid);

		grid.randomizePoolerConnections();
		
		grid.start();
		grid.whileInactive();
		
		for (int i = 1; i <= 100; i++) {
			ZGridRequest req = grid.getNewRequest();
			req.dateTime = i * 1000;
			req.inputValues[0] = req.dateTime;
			req.inputValues[1] = (i % 10) + 1;
			grid.addRequest(req);
		}
		
		sleep(5000);
		
		grid.stop();
		grid.whileActive();
		grid.destroy();
		
		msgr.stop();
		msgr.handleMessages();
		
		System.out.println(logger.toJson().toStringBuilderReadFormat());
	}

}
