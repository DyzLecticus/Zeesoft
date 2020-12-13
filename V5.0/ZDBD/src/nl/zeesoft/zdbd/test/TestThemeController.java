package nl.zeesoft.zdbd.test;

import java.util.List;

import nl.zeesoft.zdbd.ThemeController;
import nl.zeesoft.zdbd.ThemeControllerSettings;
import nl.zeesoft.zdbd.ThemeSequenceSelector;
import nl.zeesoft.zdbd.generate.Generator;
import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.Waiter;

public class TestThemeController extends TestObject {
	public TestThemeController(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestThemeController(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how a *Str* instance can be used to split a comma separated string into a list of *Str* instances. ");
		System.out.println("The *Str* class is designed to add features of the Java String to a Java StringBuilder. ");
		System.out.println("It also contains methods for file writing and reading. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Str");
		System.out.println("Str str = new Str(\"qwer,asdf,zxcv\");");
		System.out.println("// Split the Str");
		System.out.println("List<Str> strs = str.split(\",\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockStr.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestController.class));
		System.out.println(" * " + getTester().getLinkForClass(Str.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and lists the *Str* objects.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		ThemeControllerSettings settings = new ThemeControllerSettings();
		settings.soundBankDir = "../../V3.0/ZeeTracker/resources/";
		settings.workDir = "dist";
		
		ThemeController controller = new ThemeController();
		CodeRunnerChain chain = controller.initialize(settings);
		assertNotNull(chain,"Failed to initialize controller");
		if (chain!=null) {
			Waiter.startAndWaitFor(chain,10000);
			assertEqual(FileIO.getActionLog().size(),2,"Action log size does not match expectation (1)");
			assertEqual(controller.themeHasChanges(),true,"Theme changes do not match expectation (1)");
			
			System.out.println();
			chain = controller.saveTheme();
			Waiter.startAndWaitFor(chain,10000);
			assertEqual(FileIO.getActionLog().size(),24,"Action log size does not match expectation (2)");
			assertEqual(controller.themeHasChanges(),false,"Theme changes do not match expectation (2)");
			List<String> themes = controller.listThemes();
			assertEqual(themes.size(),1,"Number of themes does not match expectation");
			assertEqual(themes.get(0),"Demo","The listed theme name does not match expectation");
			assertEqual(controller.getSequences().size(),1,"Number of sequences does not match expectation (1)");

			Generator generator = new Generator();
			generator.name = "TestGenerator";
			controller.putGenerator(generator);
			assertEqual(controller.getGenerators().size(),4,"Number of generators does not match expectation");
			assertEqual(controller.themeHasChanges(),true,"Theme changes do not match expectation (3)");
			
			System.out.println();
			chain = controller.trainNetwork();
			Waiter.startAndWaitFor(chain,30000);
			
			System.out.println();
			chain = controller.generateSequence("TestGenerator");
			Waiter.startAndWaitFor(chain,10000);
			assertNotNull(controller.getGenerator("TestGenerator").generatedPatternSequence,"Generated pattern sequence does not match expectation");
			assertEqual(controller.getSequences().size(),2,"Number of sequences does not match expectation (2)");
			
			System.out.println();
			System.out.println("Playing sequence '" + NetworkTrainer.TRAINING_SEQUENCE + "'");
			ThemeSequenceSelector selector = new ThemeSequenceSelector();
			MidiSys.sequencer.addListener(selector);
			selector.setController(controller);
			selector.startSequence(NetworkTrainer.TRAINING_SEQUENCE);
			sleep(12000);
			MidiSys.sequencer.stop();
			
			sleep(1000);
			
			System.out.println();
			System.out.println("Playing theme");
			selector.startTheme("TestGenerator");
			sleep(60000);
			MidiSys.sequencer.stop();
			
			System.out.println();
			Waiter.waitFor(controller, 1000);
			chain = controller.destroy();
			Waiter.startAndWaitFor(chain,1000);
			assertEqual(FileIO.getActionLog().size(),25,"Action log size does not match expectation (3)");
			
			System.out.println();
			ThemeController controller2 = new ThemeController();
			selector.setController(controller);
			chain = controller2.initialize(settings);
			Waiter.startAndWaitFor(chain,20000);
			settings = controller2.getSettings();
			assertEqual(settings.workingTheme,"Demo","Working theme does not match expectation");
			assertEqual(FileIO.getActionLog().size(),47,"Action log size does not match expectation (4)");
			assertEqual(controller2.themeHasChanges(),false,"Theme changes do not match expectation (4)");
			
			System.out.println();
			chain = controller2.newTheme("Test");
			Waiter.startAndWaitFor(chain,10000);
			assertEqual(controller2.getName(),"Test","Active theme does not match expectation");
			assertEqual(FileIO.getActionLog().size(),47,"Action log size does not match expectation (5)");
			assertEqual(controller2.themeHasChanges(),true,"Theme changes do not match expectation (5)");
			
			System.out.println();
			chain = controller2.destroy();
			Waiter.startAndWaitFor(chain,1000);

			assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
			System.out.println();
			System.out.println(FileIO.getActionLogStr());
		}
	}
}
