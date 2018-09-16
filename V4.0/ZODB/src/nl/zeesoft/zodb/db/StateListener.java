package nl.zeesoft.zodb.db;

public interface StateListener {
	public void stateChanged(Object source,boolean open);
}
