package nl.zeesoft.zodb;

public interface StateListener {
	public void stateChanged(Object source,boolean open);
}
