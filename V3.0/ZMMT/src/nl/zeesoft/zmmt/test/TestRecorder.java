package nl.zeesoft.zmmt.test;

import java.io.File;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.recorder.Recorder;

public class TestRecorder extends TestObject {
	public TestRecorder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestRecorder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test displays the available system audio resources.");
	}

	@Override
	protected void test(String[] args) {
		Messenger msgr = new Messenger(null);
		WorkerUnion uni = new WorkerUnion(msgr);
		Recorder rec = new Recorder(msgr,uni);
		
		List<String> mixers = rec.getMixerNames();
		for (String name: mixers) {
			System.out.println(name);
		}
		System.out.println();
		
		File file = new File("test.wav");
		
		rec.intialize("Primary Sound Capture Driver");
		rec.setOutputFile(file);
		
		rec.start();
		System.out.println("Recording ...");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		rec.stop();
		System.out.println("Recorded: " + file.getAbsolutePath());

		rec.close();

		msgr.whileWorking();
	}
}
