package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Lock;

public class TestLock implements Runnable {
	private static TestLock	self	= new TestLock();
	
	private static Lock		lock	= new Lock(self) {
		@Override
		protected int getErrorAttempts() {
			assert super.getErrorAttempts() == 1000;
			return 2;
		}
	};
	
	private static boolean	locked	= false;
	
	public static void main(String[] args) {
		lock.lock();
		Thread runner = new Thread(self);
		runner.start();
		AllTests.sleep(5);
		runner.interrupt();
		AllTests.sleep(5);
		lock.unlock();
		AllTests.sleep(5);
		lock.lock();
		assert locked = true;
		lock.unlock();
	}

	@Override
	public void run() {
		lock.lock();
		if (!locked) {
			locked = true;
		}
		lock.unlock();
	}
	
	
}
