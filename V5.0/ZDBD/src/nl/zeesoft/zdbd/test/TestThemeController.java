package nl.zeesoft.zdbd.test;

import java.util.List;

import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdbd.theme.ThemeController;
import nl.zeesoft.zdbd.theme.ThemeControllerSettings;
import nl.zeesoft.zdbd.theme.ThemeSequenceSelector;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.Waiter;

public class TestThemeController extends TestObject {
	private static boolean	PLAY_SEQUENCES	= true;
	private static int		PLAY_SECONDS	= 30;
	
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
		
		System.out.println("Initializing controller ...");
		
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
			assertEqual(FileIO.getActionLog().size(),25,"Action log size does not match expectation (2)");
			assertEqual(controller.themeHasChanges(),false,"Theme changes do not match expectation (2)");
			List<String> themes = controller.listThemes();
			assertEqual(themes.size(),1,"Number of themes does not match expectation");
			assertEqual(themes.get(0),"Demo","The listed theme name does not match expectation");
			assertEqual(controller.getSequences().size(),1,"Number of sequences does not match expectation (1)");

			Generator generator = new Generator();
			generator.name = "TestGenerator";
			controller.putGenerator(generator);
			assertEqual(controller.getGenerators().size(),7,"Number of generators does not match expectation");
			assertEqual(controller.themeHasChanges(),true,"Theme changes do not match expectation (3)");
			
			System.out.println();
			chain = controller.trainNetwork();
			Waiter.startAndWaitFor(chain,60000);
			
			System.out.println();
			chain = controller.generateSequence("TestGenerator");
			Waiter.startAndWaitFor(chain,10000);
			assertNotNull(controller.getGenerator("TestGenerator").generatedPatternSequence,"Generated pattern sequence does not match expectation");
			assertEqual(controller.getSequences().size(),2,"Number of sequences does not match expectation (2)");
			
			ThemeSequenceSelector selector = new ThemeSequenceSelector();
			if (PLAY_SEQUENCES) {
				System.out.println();
				System.out.println("Playing sequence '" + NetworkTrainer.TRAINING_SEQUENCE + "'");
				MidiSys.sequencer.addListener(selector);
				selector.setController(controller);
				selector.startSequence(NetworkTrainer.TRAINING_SEQUENCE);
				sleep(11000);
				MidiSys.sequencer.stop();

				controller.setShuffle(0.2F);
				
				sleep(1000);
				MidiSys.sequencer.startRecording();
				
				System.out.println();
				System.out.println("Playing theme");
				selector.startTheme("TestGenerator");
				sleep(PLAY_SECONDS * 1000);
				MidiSys.sequencer.stop();
				
				MidiSys.sequencer.stopRecording();
				
				Waiter.waitFor(controller, 10000);
				
				chain = controller.exportRecordingTo("dist/generated.wav",false);
				Waiter.startAndWaitFor(chain,10000);
				chain = controller.exportRecordingTo("dist/generated.mid",true);
				Waiter.startAndWaitFor(chain,3000);
			}
			
			System.out.println();
			Waiter.waitFor(controller, 1000);
			chain = controller.destroy();
			Waiter.startAndWaitFor(chain,10000);
			if (assertEqual(FileIO.getActionLog().size(),26,"Action log size does not match expectation (3)")) {
				
				System.out.println();
				ThemeController controller2 = new ThemeController();
				chain = controller2.initialize(settings);
				Waiter.startAndWaitFor(chain,20000);
				if (PLAY_SEQUENCES) {
					selector.setController(controller2);
				}
				settings = controller2.getSettings();
				assertEqual(settings.workingTheme,"Demo","Working theme does not match expectation");
				assertEqual(FileIO.getActionLog().size(),49,"Action log size does not match expectation (4)");
				assertEqual(controller2.themeHasChanges(),false,"Theme changes do not match expectation (4)");
				
				System.out.println();
				chain = controller2.newTheme("Test");
				Waiter.startAndWaitFor(chain,10000);
				assertEqual(controller2.getName(),"Test","Active theme does not match expectation");
				assertEqual(FileIO.getActionLog().size(),49,"Action log size does not match expectation (5)");
				assertEqual(controller2.themeHasChanges(),true,"Theme changes do not match expectation (5)");

				chain = controller2.deleteTheme("Demo");
				Waiter.startAndWaitFor(chain,10000);
				assertEqual(FileIO.getActionLog().size(),72,"Action log size does not match expectation (6)");

				System.out.println();
				chain = controller2.destroy();
				Waiter.startAndWaitFor(chain,10000);
	
				System.out.println();
				System.out.println(FileIO.getActionLogStr());
			}
		}
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}
}
