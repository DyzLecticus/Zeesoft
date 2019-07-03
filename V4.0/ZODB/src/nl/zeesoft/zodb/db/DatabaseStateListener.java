package nl.zeesoft.zodb.db;

import nl.zeesoft.zodb.StateListener;

public interface DatabaseStateListener extends StateListener {
	public void keyChanged(StringBuilder newKey);
}
