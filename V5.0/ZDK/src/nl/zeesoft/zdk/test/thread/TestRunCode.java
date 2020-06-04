package nl.zeesoft.zdk.test.thread;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class TestRunCode extends TestObject {
	public TestRunCode(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestRunCode(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *RunCode* instance combined with a *CodeRunner* instance to run code in a separate thread. ");
		System.out.println("It also shows how to use a *CodeRunnerList* instance to run different code in multiple threads simultaneously. ");
		System.out.println("*Lock* instances are used to prevent concurrent modification exceptions. ");
		System.out.println();
		System.out.println("A *CodeRunner* will repeat calling the *RunCode*.run() method until the method returns true or the method throws an exception. ");
		System.out.println("It can also be forced to stop by calling the *CodeRunner*.stop() method. ");
		System.out.println("The *CodeRunnerList* implements similar logic; it stops when all its *CodeRunner* instances have stopped or one of them throws an exception. ");
		System.out.println("The *CodeRunnerList* can also be forced to stop by calling the *CodeRunnerList*.stop() method. ");
		System.out.println();
		System.out.println("Exceptions are caught and supressed by default but they are available through getException() methods. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the run code");
		System.out.println("RunCode code = new RunCode() {");
		System.out.println("    private int counter = 0;");
		System.out.println("    @Override");
		System.out.println("    protected boolean run() {");
		System.out.println("        counter++;");
		System.out.println("        System.out.println(\"Code run: \" + counter);");
		System.out.println("        boolean r = counter >= 10;");
		System.out.println("        if (r) {");
		System.out.println("            counter = 0;");
		System.out.println("        }");
		System.out.println("        return r;");
		System.out.println("    }");
		System.out.println("};");
		System.out.println("// Create the code runner");
		System.out.println("CodeRunner runner = new CodeRunner(code);");
		System.out.println("// Specify the number of milliseconds to sleep between runs");
		System.out.println("runner.setSleepMs(1);");
		System.out.println("// Start the runner");
		System.out.println("runner.start();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestRunCode.class));
		System.out.println(" * " + getTester().getLinkForClass(RunCode.class));
		System.out.println(" * " + getTester().getLinkForClass(CodeRunner.class));
		System.out.println(" * " + getTester().getLinkForClass(CodeRunnerList.class));
		System.out.println(" * " + getTester().getLinkForClass(Lock.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the output of several different code runs.  ");
	}

	@Override
	protected void test(String[] args) {
		System.out.println("Test 10 runs;");
		RunCode testCodeA = getNewTestRunCode();
		CodeRunner runner = new CodeRunner(testCodeA);
		runner.setSleepMs(1);
		runner.start();
		while(runner.isBusy()) {
			sleep(1);
		}
		Exception exception = runner.getCode().getException();
		assertNull(exception,"Exception does not match expectation (1)");

		// Check set logger method
		runner.setLogger(new Logger());
				
		System.out.println();
		System.out.println("Test exception handling;");
		RunCode testCodeB = new RunCode() {
			@Override
			protected boolean run() {
				Integer.parseInt("X");
				return false;
			}
		};
		runner = new CodeRunner(testCodeB);
		runner.setSleepMs(1);
		runner.start();
		while(runner.isBusy()) {
			sleep(1);
		}
		exception = runner.getCode().getException();
		assertNotNull(exception,"Exception does not match expectation (2)");
		System.out.println("Caught exception; " + exception);
		
		System.out.println();
		System.out.println("Test runner list;");
		CodeRunnerList list = new CodeRunnerList();
		list.add(testCodeA);
		list.add(getNewTestRunCode());
		list.setSleepMs(1);
		list.start();
		assertEqual(CodeRunnerManager.getActiverRunners().size(),2,"Number of active code runners does not match expectation");
		sleep(5);
		Waiter.waitTillDone(list,100);
		assertEqual(list.isBusy(),false,"List state does not match expectation");
		exception = list.getCodes().get(1).getException();
		assertNull(exception,"Exception does not match expectation (3)");

		// Check set logger method
		list.setLogger(new Logger());
		
		System.out.println();
		System.out.println("Test runner list exception handling;");
		list.add(testCodeB);
		list.start();
		Waiter.waitTillDone(list,1000);
		exception = list.getCodes().get(2).getException();
		assertNotNull(exception,"Exception does not match expectation (4)");
		System.out.println("Caught exception; " + exception);
		
		list.clearCodes();
		assertEqual(list.getCodes().size(),0,"List size does not match expectation");
		
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}

	private RunCode getNewTestRunCode() {
		return new RunCode() {
			private int counter = 0;
			@Override
			protected boolean run() {
				counter++;
				System.out.println("Code run: " + counter);
				boolean r = counter >= 10;
				if (r) {
					counter = 0;
				}
				return r;
			}
		};
	}
}
