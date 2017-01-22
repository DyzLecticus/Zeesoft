package nl.zeesoft.zodb.database.cache;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.DbConfig;

public final class CcCacheWorker extends Worker {
	private static CcCacheWorker	cacheWorker = null;
	
	private CcCacheWorker() {
		setSleep((DbConfig.getInstance().getCacheConfig().getHalfLifeSeconds() * 1000));
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public static CcCacheWorker getInstance() {
		if (cacheWorker==null) {
			cacheWorker = new CcCacheWorker();
		}
		return cacheWorker;
	}

	@Override
	public void start() {
		if (DbConfig.getInstance().getCacheConfig().isActive()) {
			Messenger.getInstance().debug(this,"Starting cache worker ...");
			super.start();
			Messenger.getInstance().debug(this,"Started cache worker");
		}
	}

	@Override
	public void stop() {
		if (isWorking()) {
			Messenger.getInstance().debug(this,"Stopping cache worker ...");
			super.stop();
			Messenger.getInstance().debug(this,"Stopped cache worker");
		}
	}

	@Override
	public void whileWorking() {
		DbConfig.getInstance().getCache().applyHalfLife(this);
	}
}
