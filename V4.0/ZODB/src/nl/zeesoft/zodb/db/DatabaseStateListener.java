package nl.zeesoft.zodb.db;

public interface DatabaseStateListener {
	public void databaseStateChanged(boolean open);
}
