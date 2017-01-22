package nl.zeesoft.zodb.database;

import java.util.Date;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public final class DbCacheSaveWorker extends Worker {
	private static DbCacheSaveWorker cacheWorker = null;
	private DbCacheSaveWorker() {
		setSleep(60000);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(); 
	}
	public static DbCacheSaveWorker getInstance() {
		if (cacheWorker==null) {
			cacheWorker = new DbCacheSaveWorker();
		}
		return cacheWorker;
	}
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting cache worker ...");
		super.start();
		Messenger.getInstance().debug(this, "Started cache worker");
	}
	@Override
	public void stop() {
		if (isWorking()) {
			Messenger.getInstance().debug(this, "Stopping cache worker ...");
			super.stop();
			while(isWorking()) {
				sleep(10);
			}
			serializeCache();
			Messenger.getInstance().debug(this, "Stopped cache worker");
		}
	}
	@Override
	public void whileWorking() {
		serializeCache();
	}
	private void serializeCache() {
		Date start = new Date();
		int size = DbCache.getInstance().serializeCacheIfChanged(this);
		if (size>0) {
			Messenger.getInstance().debug(this, "Serialize cache took: " + (new Date().getTime() - start.getTime()) + " ms (size: " + size + ")");
		}
	}
}	
