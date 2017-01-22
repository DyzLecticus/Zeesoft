package nl.zeesoft.zids.server;

import nl.zeesoft.zodb.Locker;

public class SvrControllerLocker extends Locker {
	@Override
	protected synchronized void lockMe(Object source) {
		super.lockMe(source);
	}
	@Override
	protected synchronized void unlockMe(Object source) {
		super.unlockMe(source);
	}
}
