package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Worker;

public class TestWorker {
	private static Integer		increment	= 0;
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		Worker worker = new Worker() {
			@Override
			protected void exec() {
				super.exec();
				increment++;
			}
		};
		
		assert worker.getSleepMs() == 100;
		worker.setSleepMs(1);
		assert worker.getSleepMs() == 1;
		
		assert worker.start();
		assert worker.isWorking();
		assert !worker.start();
		AllTests.sleep(10);
		assert worker.stop();
		while (worker.isWorking()) {
			AllTests.sleep(1);
		}
		assert !worker.stop();
		assert increment > 1;
	}
}
