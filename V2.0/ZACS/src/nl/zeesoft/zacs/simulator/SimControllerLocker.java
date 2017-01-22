package nl.zeesoft.zacs.simulator;

import nl.zeesoft.zodb.Locker;

public class SimControllerLocker extends Locker {	
	public void lockSim(Object source) {
		lockMe(source);
	}

	public void unlockSim(Object source) {
		unlockMe(source);
	}
	
	public boolean ifLockSim(Object source) {
		return ifLockMe(source);
	}
}
