package nl.zeesoft.zodb.database.server;

/**
 * Methods should return true if the operation is successful
 */
public interface SvrInterface  {
	public void initialize();
	public boolean install();
	public boolean open();
	public boolean close();
}
