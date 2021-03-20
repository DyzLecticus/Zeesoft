package nl.zeesoft.zdk;

public class Worker implements Runnable {
	protected Lock						lock		= new Lock(this);
	
	protected Thread					thread		= null;
	protected boolean					working		= false;
	
	protected int						sleepMs		= 100;
	
	public void setSleepMs(int sleepMs) {
		lock.lock();
		this.sleepMs = sleepMs;
		lock.unlock();
	}
	
	public int getSleepMs() {
		lock.lock();
		int r = sleepMs;
		lock.unlock();
		return r;
	}
	
	public boolean start() {
		return startWorking();
	}
	
	public boolean stop() {
		return stopWorking();
	}

	public boolean isWorking() {
		return workerIsWorking();
	}
	
	protected void exec() {
		
	}
	
	@Override
	public final void run() {
		while (!threadIsNull()) {
			exec();
			int sleep = getSleepMs();
			int slept = 0;
			while (slept < sleep) {
				Util.sleep(1);
				if (threadIsNull()) {
					break;
				}
				slept++;
				sleep = getSleepMs();
			}
		}
		lock.lock();
		working = false;
		lock.unlock();
	}
	
	private boolean startWorking() {
		boolean r = false;
		lock.lock();
		if (!working) {
			thread = new Thread(this);
			thread.start();
			working = true;
			r = true;
		}
		lock.unlock();
		return r;
	}
	
	private boolean stopWorking() {
		boolean r = false;
		lock.lock();
		if (working) {
			thread = null;
			r = true;
		}
		lock.unlock();
		return r;
	}

	private boolean workerIsWorking() {
		lock.lock();
		boolean r = working;
		lock.unlock();
		return r;
	}
	
	private boolean threadIsNull() {		
		lock.lock();
		boolean r = (thread == null);
		lock.unlock();
		return r;
	}
}
