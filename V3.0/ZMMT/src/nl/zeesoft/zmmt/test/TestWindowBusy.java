package nl.zeesoft.zmmt.test;

import javax.swing.JFrame;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zmmt.gui.WindowBusy;

public class TestWindowBusy extends TestObject {
	public TestWindowBusy(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestWindowBusy(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows the busy window for 5 seconds.");
	}

	@Override
	protected void test(String[] args) {
		JFrame parent = new JFrame();
		parent.setLocation(200,200);
		parent.setSize(200,200);
		
		WindowBusy busyWindow = new WindowBusy(new Messenger(null),parent);
		
		busyWindow.setBusy(this, "Busy", "Some details");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		busyWindow.setDone(this);
		
		parent.dispose();
	}
}
