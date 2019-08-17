package nl.zeesoft.zodb.db;

public interface DatabaseKeyChangeListener {
	public void keyChanged(StringBuilder newKey);
}
