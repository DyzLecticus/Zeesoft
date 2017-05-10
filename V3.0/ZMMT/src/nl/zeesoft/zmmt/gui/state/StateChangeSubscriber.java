package nl.zeesoft.zmmt.gui.state;

public interface StateChangeSubscriber {
	public void handleStateChange(StateChangeEvent evt);
}
