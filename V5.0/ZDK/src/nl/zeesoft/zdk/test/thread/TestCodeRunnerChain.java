package nl.zeesoft.zdk.test.thread;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.ProgressBar;
import nl.zeesoft.zdk.thread.ProgressListener;
import nl.zeesoft.zdk.thread.RunCode;

public class TestCodeRunnerChain extends TestObject {
	public TestCodeRunnerChain(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestCodeRunnerChain(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *CodeRunnerChain* instance to run a sequence of *CodeRunnerList* instances in a separate threads. ");
		System.out.println("Classes that implement *ProgressListener* can be used to listen to the progress of all *RunCode* instances in the entire chain. ");
		System.out.println("An example of this is a *ProgressBar* that logs the chain progress to the console standard output. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the chain");
		System.out.println("CodeRunnerChain chain = new CodeRunnerChain();");
		System.out.println("// Add a list of codes");
		System.out.println("chain.addAll(new ArrayList<RunCode>());");
		System.out.println("// Add a single code");
		System.out.println("chain.add(new RunCode());");
		System.out.println("// Add a progress listener to the chain");
		System.out.println("chain.addProgressListener(new ProgressBar(\"Process description\"));");
		System.out.println("// Start the chain");
		System.out.println("chain.start();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestCodeRunnerChain.class));
		System.out.println(" * " + getTester().getLinkForClass(CodeRunnerChain.class));
		System.out.println(" * " + getTester().getLinkForClass(CodeRunnerList.class));
		System.out.println(" * " + getTester().getLinkForClass(ProgressListener.class));
		System.out.println(" * " + getTester().getLinkForClass(ProgressBar.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * An example chain that imports multiple files simultaneously and then processes the results.  ");
		System.out.println(" * An example of multiple chains using progress bars to show their progress simultaneously.  ");
	}

	@Override
	protected void test(String[] args) {
		System.out.println("File import;");
		// Basic chain
		CodeRunnerChain chain = new CodeRunnerChain();
		List<RunCode> codes = new ArrayList<RunCode>();
		for (int i = 0; i < 4; i++) {
			codes.add(getNewTestRunCode("Reading file: " + (i + 1)));
		}
		chain.addAll(codes);
		chain.add(getNewTestRunCode("Imported 4 files"));
		chain.start();
		while(chain.isBusy()) {
			sleep(10);
		}
		assertEqual(chain.getDoneCodes(),5,"Number of done codes does not match expectation");
		
		// Progress bar
		System.out.println();
		System.out.println("Progress bars;");
		chain = new CodeRunnerChain();
		codes = new ArrayList<RunCode>();
		for (int i = 0; i < 9; i++) {
			codes.add(getNewTestRunCode("",50));
		}
		chain.addAll(codes);
		chain.add(getNewTestRunCode("",50));
		chain.addProgressListener(new ProgressBar("Primary tasks"));

		CodeRunnerChain chain2 = new CodeRunnerChain();
		codes = new ArrayList<RunCode>();
		for (int i = 0; i < 9; i++) {
			codes.add(getNewTestRunCode("",51));
		}
		chain2.addAll(codes);
		chain2.add(getNewTestRunCode("",51));
		chain2.addProgressListener(new ProgressBar("Secondary tasks"));

		chain.start();
		chain2.start();
		while(chain.isBusy()) {
			sleep(10);
		}
		while(chain2.isBusy()) {
			sleep(10);
		}
		assertEqual(chain.getDoneCodes(),10,"Number of done codes does not match expectation(1)");
		assertEqual(chain2.getDoneCodes(),10,"Number of done codes does not match expectation(2)");

		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}
	
	private RunCode getNewTestRunCode(String message) {
		return getNewTestRunCode(message, 0);
	}

	private RunCode getNewTestRunCode(String message, int sleep) {
		return new RunCode() {
			@Override
			protected boolean run() {
				if (message.length()>0) {
					System.out.println(message);
				}
				if (sleep>0) {
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		};
	}
}
