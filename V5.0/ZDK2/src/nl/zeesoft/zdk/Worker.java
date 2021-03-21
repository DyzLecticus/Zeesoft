package nl.zeesoft.zdk;

public class Worker implements Runnable {
	protected Lock		lock			= new Lock(this);
	
	protected Thread	thread			= null;
	protected boolean	working			= false;
	
	protected int		sleepMs			= 100;
	
	protected int		minSleepMs		= 100;
	protected long		noSleepStart	= 0;
	
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
	
	public void setMinSleepMs(int minSleepMs) {
		lock.lock();
		this.minSleepMs = minSleepMs;
		lock.unlock();
	}
	
	public int getMinSleepMs() {
		lock.lock();
		int r = minSleepMs;
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
			int minSleep = getMinSleepMs();
			if (sleep>0) {
				int slept = 0;
				while (slept < sleep) {
					Util.sleep(1);
					if (threadIsNull()) {
						break;
					}
					slept++;
					sleep = getSleepMs();
				}
			} else if (minSleep>0){
				if (noSleepStart == 0) {
					noSleepStart = System.currentTimeMillis();
				} else {
					long diff = System.currentTimeMillis() - noSleepStart;
					if (diff>minSleep) {
						setSleepMs(minSleep);
					}
				}
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
